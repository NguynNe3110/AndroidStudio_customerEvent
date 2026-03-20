package com.uzuu.customer.feature.middle.ticket

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.uzuu.customer.databinding.FragmentTicketBinding
import com.uzuu.customer.feature.MainActivity
import com.uzuu.customer.ui.adapter.MyTicketAdapter
import kotlinx.coroutines.launch

class TicketFragment : Fragment() {

    private var _binding: FragmentTicketBinding? = null
    val binding get() = _binding!!

    private lateinit var ticketAdapter: MyTicketAdapter

    private val viewModel: TicketViewModel by viewModels {
        val repo = (requireActivity() as MainActivity).container.myTicketRepo
        TicketFactory(repo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecycler()
        observeState()
        observeEvent()
    }

    // Reload mỗi lần quay lại tab (vé có thể vừa được mua)
    override fun onResume() {
        super.onResume()
        viewModel.loadTickets()
    }

    private fun setupRecycler() {
        ticketAdapter = MyTicketAdapter { ticket ->
            // Click vào vé → mở bottom sheet hiển thị chi tiết + QR
            if (parentFragmentManager.findFragmentByTag("ticket_detail") != null) return@MyTicketAdapter
            TicketDetailBottomSheet(ticket).show(parentFragmentManager, "ticket_detail")
        }
        binding.recyclerTickets.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ticketAdapter
            setHasFixedSize(false)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE

                    val isEmpty = state.tickets.isEmpty() && !state.isLoading
                    binding.tvEmpty.visibility     = if (isEmpty) View.VISIBLE else View.GONE
                    binding.recyclerTickets.visibility = if (!isEmpty) View.VISIBLE else View.GONE

                    ticketAdapter.submitList(state.tickets)
                }
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is TicketUiEvent.Toast ->
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
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