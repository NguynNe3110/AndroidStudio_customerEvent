package com.uzuu.customer.feature.middle.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.uzuu.customer.databinding.FragmentCartBinding
import com.uzuu.customer.feature.MainActivity
import com.uzuu.customer.ui.adapter.CartItemAdapter
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    val binding get() = _binding!!

    private lateinit var cartAdapter: CartItemAdapter

    private val viewModel: CartViewModel by viewModels {
        val cartRepo  = (requireActivity() as MainActivity).container.cartRepo
        val orderRepo = (requireActivity() as MainActivity).container.orderRepo
        CartFactory(cartRepo, orderRepo)
    }

    private val fmt = NumberFormat.getNumberInstance(Locale("vi", "VN"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecycler()
        setupPaymentDropdown()
        setupButtons()
        observeState()
        observeEvent()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCart()
    }

    private fun setupRecycler() {
        cartAdapter = CartItemAdapter(
            onCheckedChange = { itemId, checked ->
                // Nếu user tick thủ công → cập nhật state
                viewModel.toggleItemSelection(itemId)
            },
            onQuantityChange = { itemId, qty -> viewModel.updateItemQuantity(itemId, qty) }
        )
        binding.recyclerCart.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
            setHasFixedSize(false)
        }
    }

    private fun setupPaymentDropdown() {
        val methods = listOf("BANKING", "MOMO", "VNPAY")
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_dropdown_item_1line, methods
        )
        binding.dropdownPayment.setAdapter(adapter)
        binding.dropdownPayment.setOnItemClickListener { _, _, pos, _ ->
            viewModel.onPaymentSelected(methods[pos])
        }
    }

    private fun setupButtons() {
        binding.btnClearCart.setOnClickListener { viewModel.onClearCart() }
        binding.btnCheckout.setOnClickListener { viewModel.onCheckout() }
        binding.btnCheckoutSelected.setOnClickListener { viewModel.onCheckoutSelected() }
        binding.btnDeleteSelected.setOnClickListener { viewModel.deleteSelectedItems() }

        binding.checkboxSelectAll.setOnCheckedChangeListener { _, _ ->
            viewModel.toggleSelectAll()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartState.collect { state ->
                    // Loading
                    binding.progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE

                    // Empty / content
                    val isEmpty = state.items.isEmpty() && !state.isLoading
                    binding.tvEmpty.visibility         = if (isEmpty) View.VISIBLE else View.GONE
                    binding.recyclerCart.visibility    = if (!isEmpty) View.VISIBLE else View.GONE
                    binding.cardCheckout.visibility    = if (!isEmpty) View.VISIBLE else View.GONE
                    binding.toolbarSelection.visibility = if (!isEmpty) View.VISIBLE else View.GONE

                    // Danh sách
                    cartAdapter.selectedIds = state.selectedItemIds
                    cartAdapter.submitList(state.items)

                    // Checkbox chọn tất cả (tránh vòng lặp)
                    binding.checkboxSelectAll.setOnCheckedChangeListener(null)
                    binding.checkboxSelectAll.isChecked = state.isAllSelected
                    binding.checkboxSelectAll.setOnCheckedChangeListener { _, _ ->
                        viewModel.toggleSelectAll()
                    }

                    // Hiển thị tổng tiền: nếu đang chọn → hiện tổng đã chọn
                    val totalToShow = if (state.hasSelection) state.selectedTotal else state.totalAmount
                    val label = if (state.hasSelection)
                        "Đã chọn (${state.selectedItemIds.size}):"
                    else
                        "Tổng cộng:"
                    binding.tvTotalLabel.text = label
                    binding.tvTotal.text = "${fmt.format(totalToShow.toLong())}đ"

                    // Nút checkout-selected chỉ active khi có lựa chọn
                    binding.btnCheckoutSelected.isEnabled = state.hasSelection
                    binding.btnDeleteSelected.isEnabled   = state.hasSelection

                    binding.dropdownPayment.setText(state.selectedPayment, false)
                }
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartEvent.collect { event ->
                    when (event) {
                        is CartUiEvent.Toast ->
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                        is CartUiEvent.CheckoutSuccess -> viewModel.loadCart()
                        is CartUiEvent.CartCleared,
                        is CartUiEvent.ItemDeleted -> { /* state đã cập nhật trong VM */ }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}