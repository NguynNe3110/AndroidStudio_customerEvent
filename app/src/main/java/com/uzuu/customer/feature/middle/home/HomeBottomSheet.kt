package com.uzuu.customer.feature.middle.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.uzuu.customer.databinding.BottomsheetEventBinding
import com.uzuu.customer.domain.model.Event
import com.uzuu.customer.ui.adapter.CategoryTicketAdapter
import com.uzuu.customer.ui.adapter.EventAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeBottomSheet(
    private val event: Event,
    private val onAddToCart: suspend (ticketTypeId: Long, quantity: Int) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomsheetEventBinding? = null
    private val binding get() = _binding!!

    private val ticketAdapter = CategoryTicketAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindEventInfo()
        setupTicketRecycler()
        setupButtons()
    }

    private fun bindEventInfo() {
        binding.txtNameEvent.text     = event.name
        binding.txtAddress.text       = "📍 ${event.location}"
        binding.txtDateTimeStart.text = "Bắt đầu: ${event.startTime ?: "Chưa xác định"}"
        binding.txtDateTimeEnd.text   = "Kết thúc: ${event.endTime ?: "Chưa xác định"}"
    }

    private fun setupTicketRecycler() {
        binding.recyclerCategoryTicket.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ticketAdapter
            setHasFixedSize(false)
        }
        ticketAdapter.submitList(event.ticketTypes)
    }

    private fun setupButtons() {
        binding.handleBar.setOnClickListener { dismiss() }

        // ── Xem chi tiết → navigate sang EventDetailFragment ────────────────
        binding.txtViewDetail.setOnClickListener {
            dismiss()
            // Tìm navController từ fragment cha (HomeFragment nằm trong main_graph)
            val parentFragment = parentFragmentManager.fragments
                .filterIsInstance<HomeFragment>()
                .firstOrNull()
            parentFragment?.findNavController()
                ?.navigate(HomeFragmentDirections.actionHomeFragmentToEventDetail(event))
        }

        // ── Thêm vào giỏ ────────────────────────────────────────────────────
        binding.btnAddToCart.setOnClickListener {
            val selected = ticketAdapter.getSelectedQuantities()
            if (selected.isEmpty()) {
                Toast.makeText(context, "Vui lòng chọn ít nhất 1 vé", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnAddToCart.isEnabled = false
            lifecycleScope.launch(Dispatchers.IO) {
                var hasError = false
                selected.forEach { (ticketTypeId, qty) ->
                    try { onAddToCart(ticketTypeId, qty) }
                    catch (e: Exception) { hasError = true }
                }
                launch(Dispatchers.Main) {
                    binding.btnAddToCart.isEnabled = true
                    if (hasError) {
                        Toast.makeText(context, "Có lỗi khi thêm vé, thử lại", Toast.LENGTH_SHORT).show()
                    } else {
                        val summary = selected.entries.joinToString(", ") { (id, qty) ->
                            val ticket = event.ticketTypes.find { it.id == id }
                            "${ticket?.name ?: id} ×$qty"
                        }
                        Toast.makeText(context, "✓ Đã thêm: $summary", Toast.LENGTH_SHORT).show()
                        ticketAdapter.resetQuantities()
                        dismiss()
                    }
                }
            }
        }

        // ── Mua ngay ────────────────────────────────────────────────────────
        binding.btnBuyNow.setOnClickListener {
            val selected = ticketAdapter.getSelectedQuantities()
            if (selected.isEmpty()) {
                Toast.makeText(context, "Vui lòng chọn ít nhất 1 vé", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnBuyNow.isEnabled = false
            lifecycleScope.launch(Dispatchers.IO) {
                selected.forEach { (ticketTypeId, qty) -> onAddToCart(ticketTypeId, qty) }
                launch(Dispatchers.Main) {
                    binding.btnBuyNow.isEnabled = true
                    Toast.makeText(context, "Đã thêm vào giỏ – chuyển đến trang thanh toán", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ticketAdapter.resetQuantities()
        _binding = null
    }
}