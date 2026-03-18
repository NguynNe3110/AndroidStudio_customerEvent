package com.uzuu.customer.data.remote.datasource

import com.uzuu.customer.data.remote.api.CategoryApi
import com.uzuu.customer.data.remote.dto.BaseResponseDto
import com.uzuu.customer.data.remote.dto.response.CategoryEventResponseDto

class CategoryRemoteDataSource(
    private val categoryApi: CategoryApi
) {
    suspend fun getAllCategories(): BaseResponseDto<List<CategoryEventResponseDto>> {
        return categoryApi.getAllCategories()
    }
}