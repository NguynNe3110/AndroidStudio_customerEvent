package com.uzuu.customer.data.remote.api

import com.uzuu.customer.data.remote.dto.BaseResponseDto
import com.uzuu.customer.data.remote.dto.response.CategoryEventResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoryApi {
    @GET("categories")
    suspend fun getAllCategories(
    ): BaseResponseDto<List<CategoryEventResponseDto>>

    @GET("categories/{id}")
    suspend fun getCategoryById(
        @Path("id") id: Int
    ) : BaseResponseDto<CategoryEventResponseDto>
}