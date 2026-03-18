package com.uzuu.customer.feature.middle.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzuu.customer.domain.model.CategoryItem
import com.uzuu.customer.domain.repository.CategoryRepository
import com.uzuu.customer.domain.repository.EventRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val eventRepo: EventRepository,
    private val categoryRepo: CategoryRepository
) : ViewModel() {

    private var currentPage = 0
    private val _homeState = MutableStateFlow(HomeUiState())
    val homeState = _homeState.asStateFlow()

    private val _homeEvent = MutableSharedFlow<HomeUiEvent>(extraBufferCapacity = 3)
    val homeEvent = _homeEvent.asSharedFlow()

    fun init() {
        println("DEBUG [HomeViewModel] init() called — currentPage=$currentPage")

        viewModelScope.launch {
            _homeState.update { it.copy(isLoading = true) }

            // Load categories và events song song
            val categoriesDeferred = async {
                try {
                    println("DEBUG [HomeViewModel] loading categories...")
                    val result = categoryRepo.getAllCategories()
                    println("DEBUG [HomeViewModel] categories OK: ${result.size} items → $result")
                    result
                } catch (e: Exception) {
                    println("DEBUG [HomeViewModel] ERROR categories: ${e::class.simpleName} — ${e.message}")
                    e.printStackTrace()
                    emptyList()
                }
            }

            val eventsDeferred = async {
                try {
                    println("DEBUG [HomeViewModel] loading events page 0...")
                    val result = eventRepo.getEvent(0)
                    println("DEBUG [HomeViewModel] events OK: ${result.data.size} items, isLast=${result.isLast}, totalPages=${result.totalPages}")
                    result
                } catch (e: Exception) {
                    println("DEBUG [HomeViewModel] ERROR events: ${e::class.simpleName} — ${e.message}")
                    e.printStackTrace()
                    null
                }
            }

            val categories = categoriesDeferred.await()
            val eventsResult = eventsDeferred.await()
            val events = eventsResult?.data ?: emptyList()

            println("DEBUG [HomeViewModel] init() done — categories=${categories.size}, events=${events.size}")

            _homeState.update { state ->
                state.copy(
                    isLoading = false,
                    categories = categories,
                    allEvents = events,
                    events = events,
                    isLastPage = eventsResult?.isLast ?: true
                )
            }

            currentPage = 1
            println("DEBUG [HomeViewModel] state after init — isLastPage=${_homeState.value.isLastPage}, currentPage=$currentPage")
        }
    }

    fun loadMoreEvents() {
        val state = _homeState.value
        println("DEBUG [HomeViewModel] loadMoreEvents() — isLoading=${state.isLoading}, isLastPage=${state.isLastPage}, page=$currentPage")

        if (state.isLoading || state.isLastPage) return

        viewModelScope.launch {
            _homeState.update { it.copy(isLoading = true) }
            try {
                val result = eventRepo.getEvent(currentPage)
                println("DEBUG [HomeViewModel] loadMore page=$currentPage OK: ${result.data.size} items")
                currentPage++
                _homeState.update { s ->
                    val newAll = s.allEvents + result.data
                    val filtered = filterByCategory(newAll, s.selectedCategoryId, s.categories)
                    s.copy(allEvents = newAll, events = filtered, isLoading = false, isLastPage = result.isLast)
                }
            } catch (e: Exception) {
                println("DEBUG [HomeViewModel] ERROR loadMore: ${e::class.simpleName} — ${e.message}")
                e.printStackTrace()
                _homeState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onCategorySelected(category: CategoryItem) {
        println("DEBUG [HomeViewModel] onCategorySelected: id=${category.id}, name='${category.name}'")
        _homeState.update { state ->
            val updatedCategories = state.categories.map { it.copy(isSelected = it.id == category.id) }
            val filtered = filterByCategory(state.allEvents, category.id, updatedCategories)
            println("DEBUG [HomeViewModel] filtered: ${filtered.size} events")
            state.copy(categories = updatedCategories, selectedCategoryId = category.id, events = filtered)
        }
    }

    private fun filterByCategory(
        events: List<com.uzuu.customer.domain.model.Event>,
        selectedId: Int,
        categories: List<CategoryItem>
    ): List<com.uzuu.customer.domain.model.Event> {
        if (selectedId == -1) return events
        val selectedName = categories.find { it.id == selectedId }?.name
        println("DEBUG [HomeViewModel] filterByCategory: id=$selectedId, name='$selectedName'")
        if (selectedName == null) return events
        return events.filter { it.categoryName == selectedName }
    }
}