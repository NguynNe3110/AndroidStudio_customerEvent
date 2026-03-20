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
        val orderRepo = (requireActivity() as MainActivity).container.orderRepo   // ← thêm
        CartFactory(cartRepo, orderRepo)
    }

    private val priceFormatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
        cartAdapter = CartItemAdapter()
        binding.recyclerCart.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
            setHasFixedSize(false)
        }
    }

    private fun setupPaymentDropdown() {
        val methods = listOf("BANKING", "MOMO", "VNPAY")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, methods)
        binding.dropdownPayment.setAdapter(adapter)
        binding.dropdownPayment.setOnItemClickListener { _, _, position, _ ->
            viewModel.onPaymentSelected(methods[position])
        }
    }

    private fun setupButtons() {
        binding.btnClearCart.setOnClickListener { viewModel.onClearCart() }
        binding.btnCheckout.setOnClickListener  { viewModel.onCheckout() }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartState.collect { state ->
                    binding.progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE

                    val isEmpty = state.items.isEmpty() && !state.isLoading
                    binding.tvEmpty.visibility      = if (isEmpty) View.VISIBLE else View.GONE
                    binding.recyclerCart.visibility = if (!isEmpty) View.VISIBLE else View.GONE
                    binding.cardCheckout.visibility = if (!isEmpty) View.VISIBLE else View.GONE

                    cartAdapter.submitList(state.items)
                    binding.tvTotal.text = "${priceFormatter.format(state.totalAmount.toLong())}đ"
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
                        is CartUiEvent.Toast         ->
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()

                        is CartUiEvent.CheckoutSuccess -> {
                            // Server đã xoá cart sau checkout → reload để hiện empty state
                            viewModel.loadCart()
                        }

                        is CartUiEvent.CartCleared   -> { /* state đã reset trong VM */ }
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