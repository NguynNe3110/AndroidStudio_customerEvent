package com.uzuu.customer.feature.middle.home

import com.uzuu.customer.domain.model.CategoryItem
import com.uzuu.customer.domain.model.Event

data class HomeUiState(
    val isLoading: Boolean = false,
    val isLastPage: Boolean = false,
    // Toàn bộ event từ server, dùng để filter
    val allEvents: List<Event> = emptyList(),
    // Event đang hiển thị (sau khi filter theo category + search)
    val events: List<Event> = emptyList(),
    val categories: List<CategoryItem> = emptyList(),
    val selectedCategoryId: Int = -1,   // -1 = Tất cả
    val searchQuery: String = ""        // ← THÊM: từ khóa tìm kiếm
)