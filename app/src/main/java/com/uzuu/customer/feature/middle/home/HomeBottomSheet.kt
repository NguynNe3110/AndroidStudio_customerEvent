package com.uzuu.customer.feature.middle.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.uzuu.customer.databinding.BottomsheetEventBinding
import com.uzuu.customer.domain.model.Event
import com.uzuu.customer.ui.adapter.CategoryTicketAdapter

class HomeBottomSheet(
    private val event: Event
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
        binding.txtNameEvent.text = event.name
        binding.txtAddress.text = "📍 ${event.location}"
        binding.txtDateTimeStart.text = "Bắt đầu: ${event.startTime}"
        binding.txtDateTimeEnd.text = "Kết thúc: ${event.endTime}"
    }

    private fun setupTicketRecycler() {
        binding.recyclerCategoryTicket.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ticketAdapter
            setHasFixedSize(false)
        }
        // Submit danh sách loại vé của sự kiện này
        ticketAdapter.submitList(event.ticketTypes)
    }

    private fun setupButtons() {
        binding.txtViewDetail.setOnClickListener {
            // TODO: Navigate to event detail screen
            Toast.makeText(context, "Xem chi tiết: ${event.name}", Toast.LENGTH_SHORT).show()
        }

        binding.handleBar.setOnClickListener {
            dismiss()
        }

        binding.btnAddToCart.setOnClickListener {
            val selected = ticketAdapter.getSelectedQuantities()
            if (selected.isEmpty()) {
                Toast.makeText(context, "Vui lòng chọn ít nhất 1 vé", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // TODO: Gọi API POST /cart/add cho từng loại vé
            val summary = selected.entries.joinToString("\n") { (id, qty) ->
                val ticket = event.ticketTypes.find { it.id == id }
                "${ticket?.name ?: id}: $qty vé"
            }
            Toast.makeText(context, "Thêm vào giỏ:\n$summary", Toast.LENGTH_LONG).show()
        }

        binding.btnBuyNow.setOnClickListener {
            val selected = ticketAdapter.getSelectedQuantities()
            if (selected.isEmpty()) {
                Toast.makeText(context, "Vui lòng chọn ít nhất 1 vé", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // TODO: Gọi API POST /cart/add rồi POST /orders/checkout
            Toast.makeText(context, "Tính năng mua ngay đang phát triển", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ticketAdapter.resetQuantities()
        _binding = null
    }
}