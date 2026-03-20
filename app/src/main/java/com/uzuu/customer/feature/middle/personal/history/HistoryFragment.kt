package com.uzuu.customer.feature.middle.personal.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uzuu.customer.databinding.FragmentHistoryBinding
import com.uzuu.customer.feature.MainActivity
import com.uzuu.customer.ui.adapter.OrderAdapter
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    val binding get() = _binding!!

    private lateinit var orderAdapter: OrderAdapter

    private val viewModel: HistoryViewModel by viewModels {
        val orderRepo = (requireActivity() as MainActivity).container.orderRepo
        HistoryFactory(orderRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecycler()
        setupPagination()
        observeState()
        observeEvent()

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        viewModel.loadOrders()
    }

    private fun setupRecycler() {
        orderAdapter = OrderAdapter()
        binding.recyclerOrders.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = orderAdapter
            setHasFixedSize(false)
        }
    }

    private fun setupPagination() {
        binding.recyclerOrders.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0) return
                val lm = recyclerView.layoutManager as LinearLayoutManager
                if (lm.findLastVisibleItemPosition() >= lm.itemCount - 3) {
                    viewModel.loadOrders()
                }
            }
        })
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE

                    val isEmpty = state.orders.isEmpty() && !state.isLoading
                    binding.tvEmpty.visibility      = if (isEmpty) View.VISIBLE else View.GONE
                    binding.recyclerOrders.visibility = if (!isEmpty) View.VISIBLE else View.GONE

                    orderAdapter.submitList(state.orders)
                }
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}