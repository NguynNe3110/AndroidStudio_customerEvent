package com.uzuu.customer.domain.repository

import com.uzuu.customer.domain.model.CategoryItem

interface CategoryRepository {
    suspend fun getAllCategories(): List<CategoryItem>
}