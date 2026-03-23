package com.uzuu.customer.feature.middle.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzuu.customer.domain.model.CategoryItem
import com.uzuu.customer.domain.model.Event
import com.uzuu.customer.domain.repository.CategoryRepository
import com.uzuu.customer.domain.repository.EventRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
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

    private var currentPage = 1
    private val _homeState = MutableStateFlow(HomeUiState())
    val homeState = _homeState.asStateFlow()

    private val _homeEvent = MutableSharedFlow<HomeUiEvent>(extraBufferCapacity = 3)
    val homeEvent = _homeEvent.asSharedFlow()

    private var pollingJob: Job? = null

    fun init() {
        viewModelScope.launch {
            _homeState.update { it.copy(isLoading = true) }

            val categoriesDeferred = async {
                try {
                    val result = categoryRepo.getAllCategories()
                    println("DEBUG [HomeViewModel] categories OK: ${result.size}")
                    result
                } catch (e: Exception) {
                    val msg = e.message ?: ""
                    println("DEBUG [HomeViewModel] ERROR categories: $msg")
                    if (msg.contains("401")) {
                        _homeEvent.emit(HomeUiEvent.Toast("Phiên đăng nhập hết hạn"))
                        _homeEvent.emit(HomeUiEvent.navigateBack)
                    }
                    emptyList<CategoryItem>()
                }
            }

            val eventsDeferred = async {
                try {
                    val result = eventRepo.getEvent(1)
                    println("DEBUG [HomeViewModel] events OK: ${result.data.size}")
                    result
                } catch (e: Exception) {
                    val msg = e.message ?: ""
                    println("DEBUG [HomeViewModel] ERROR events: $msg")
                    if (msg.contains("401")) {
                        _homeEvent.emit(HomeUiEvent.Toast("Phiên đăng nhập hết hạn"))
                        _homeEvent.emit(HomeUiEvent.navigateBack)
                    }
                    null
                }
            }

            val categories = categoriesDeferred.await()
            val eventsResult = eventsDeferred.await()
            val events = eventsResult?.data ?: emptyList()

            _homeState.update { state ->
                state.copy(
                    isLoading = false,
                    categories = categories,
                    allEvents = events,
                    events = events,
                    isLastPage = eventsResult?.isLast ?: true
                )
            }

            currentPage = 2
        }
    }

    fun loadMoreEvents() {
        val state = _homeState.value
        if (state.isLoading || state.isLastPage) return

        viewModelScope.launch {
            _homeState.update { it.copy(isLoading = true) }
            try {
                val result = eventRepo.getEvent(currentPage)
                currentPage++
                _homeState.update { s ->
                    val newAll = s.allEvents + result.data
                    val filtered = filterByCategoryAndQuery(newAll, s.selectedCategoryId, s.categories, s.searchQuery)
                    s.copy(
                        allEvents = newAll,
                        events = filtered,
                        isLoading = false,
                        isLastPage = result.isLast
                    )
                }
            } catch (e: Exception) {
                val msg = e.message ?: ""
                println("DEBUG [HomeViewModel] loadMore ERROR: $msg")
                if (msg.contains("401")) {
                    _homeEvent.emit(HomeUiEvent.Toast("Phiên đăng nhập hết hạn, vui lòng đăng nhập lại"))
                    _homeEvent.emit(HomeUiEvent.navigateBack)
                }
                _homeState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onCategorySelected(category: CategoryItem) {
        _homeState.update { state ->
            val updatedCategories = state.categories.map { it.copy(isSelected = it.id == category.id) }
            val filtered = filterByCategoryAndQuery(state.allEvents, category.id, updatedCategories, state.searchQuery)
            state.copy(
                categories = updatedCategories,
                selectedCategoryId = category.id,
                events = filtered
            )
        }
    }

    // ── THÊM MỚI: tìm kiếm theo tên ─────────────────────────────────────────
    fun onSearch(query: String) {
        _homeState.update { state ->
            val filtered = filterByCategoryAndQuery(
                events     = state.allEvents,
                categoryId = state.selectedCategoryId,
                categories = state.categories,
                query      = query
            )
            state.copy(searchQuery = query, events = filtered)
        }
    }

    // ── Helper: lọc kết hợp category + từ khóa ──────────────────────────────
    private fun filterByCategoryAndQuery(
        events: List<Event>,
        categoryId: Int,
        categories: List<CategoryItem>,
        query: String
    ): List<Event> {
        var result = events

        // Lọc theo category
        if (categoryId != -1) {
            val selectedName = categories.find { it.id == categoryId }?.name
            if (selectedName != null) {
                result = result.filter { it.categoryName == selectedName }
            }
        }

        // Lọc theo tên sự kiện (case-insensitive)
        if (query.isNotBlank()) {
            result = result.filter {
                it.name.contains(query.trim(), ignoreCase = true)
            }
        }

        return result
    }

    fun startPolling(intervalMs: Long = 30_000L) {
        stopPolling()
        pollingJob = viewModelScope.launch {
            while (true) {
                delay(intervalMs)
                refresh()  // hàm refresh() ở Cách 1
                println("DEBUG [HomeViewModel] polling refreshed")
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                val result = eventRepo.getEvent(1)
                currentPage = 2
                _homeState.update { state ->
                    val filtered = filterByCategoryAndQuery(
                        events     = result.data,
                        categoryId = state.selectedCategoryId,
                        categories = state.categories,
                        query      = state.searchQuery
                    )
                    state.copy(
                        allEvents  = result.data,
                        events     = filtered,
                        isLastPage = result.isLast
                    )
                }
            } catch (e: Exception) {
                println("DEBUG [HomeViewModel] refresh ERROR: ${e.message}")
            }
        }
    }
}