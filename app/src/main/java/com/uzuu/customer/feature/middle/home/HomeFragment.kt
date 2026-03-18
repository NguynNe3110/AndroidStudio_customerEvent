package com.uzuu.customer.feature.middle.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uzuu.customer.databinding.FragmentHomeBinding
import com.uzuu.customer.domain.model.Event
import com.uzuu.customer.feature.MainActivity
import com.uzuu.customer.ui.adapter.CategoryAdapter
import com.uzuu.customer.ui.adapter.EventAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var eventAdapter: EventAdapter

    private val viewModel: HomeViewModel by viewModels {
        val eventRepo    = (requireActivity() as MainActivity).container.eventRepo
        val categoryRepo = (requireActivity() as MainActivity).container.categoryRepo
        println("DEBUG [HomeFragment] creating ViewModel — eventRepo=$eventRepo, categoryRepo=$categoryRepo")
        HomeFactory(eventRepo, categoryRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        println("DEBUG [HomeFragment] onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("DEBUG [HomeFragment] onViewCreated")
        setupAdapters()
        observeState()
        setupPagination()
        viewModel.init()
    }

    private fun setupAdapters() {
        println("DEBUG [HomeFragment] setupAdapters()")

        categoryAdapter = CategoryAdapter { clickedCategory ->
            println("DEBUG [HomeFragment] category clicked: id=${clickedCategory.id}, name='${clickedCategory.name}'")
            viewModel.onCategorySelected(clickedCategory)
        }
        binding.recyclerCategory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
            setHasFixedSize(true)
        }
        println("DEBUG [HomeFragment] recyclerCategory set up — id=${binding.recyclerCategory.id}")

        eventAdapter = EventAdapter { event ->
            println("DEBUG [HomeFragment] event clicked: id=${event.id}, name='${event.name}'")
            showBottomSheet(event)
        }
        binding.recyclerEvent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
            setHasFixedSize(true)
        }
        println("DEBUG [HomeFragment] recyclerEvent set up — id=${binding.recyclerEvent.id}")
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeState.collect { state ->
                    println("DEBUG [HomeFragment] state collected — categories=${state.categories.size}, events=${state.events.size}, isLoading=${state.isLoading}")

                    categoryAdapter.submitList(state.categories)
                    eventAdapter.submitList(state.events)
                }
            }
        }
    }

    private fun setupPagination() {
        binding.recyclerEvent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0) return
                val lm = recyclerView.layoutManager as LinearLayoutManager
                val lastVisible = lm.findLastVisibleItemPosition()
                val total = lm.itemCount
                if (lastVisible >= total - 3) {
                    println("DEBUG [HomeFragment] near bottom — lastVisible=$lastVisible, total=$total → loadMoreEvents()")
                    viewModel.loadMoreEvents()
                }
            }
        })
    }

    private fun showBottomSheet(event: Event) {
        if (parentFragmentManager.findFragmentByTag("event_bottom_sheet") != null) {
            println("DEBUG [HomeFragment] bottomSheet already showing, skip")
            return
        }
        println("DEBUG [HomeFragment] showing BottomSheet for event: id=${event.id}")
        HomeBottomSheet(event).show(parentFragmentManager, "event_bottom_sheet")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("DEBUG [HomeFragment] onDestroyView")
        _binding = null
    }
}