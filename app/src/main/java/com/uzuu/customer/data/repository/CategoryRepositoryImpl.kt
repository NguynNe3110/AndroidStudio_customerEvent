package com.uzuu.customer.data.repository

import com.uzuu.customer.data.remote.datasource.CategoryRemoteDataSource
import com.uzuu.customer.domain.model.CategoryItem
import com.uzuu.customer.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val remote: CategoryRemoteDataSource
) : CategoryRepository {
    override suspend fun getAllCategories(): List<CategoryItem> {
        val response = remote.getAllCategories()
        // Thêm "Tất cả" ở đầu danh sách với id = -1
        val allCategory = CategoryItem(id = -1, name = "Tất cả", isSelected = true)
        val fromServer = response.result.map {
            CategoryItem(
                id = it.id.toInt(),
                name = it.name,
                isSelected = false
            )
        }
        return listOf(allCategory) + fromServer
    }
}