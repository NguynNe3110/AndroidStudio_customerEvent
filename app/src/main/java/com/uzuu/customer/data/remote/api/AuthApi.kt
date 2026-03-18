package com.uzuu.customer.data.remote.api

import com.uzuu.customer.data.remote.dto.BaseResponseDto
import com.uzuu.customer.data.remote.dto.request.LoginRequestDto
import com.uzuu.customer.data.remote.dto.request.RegisterRequestDto
import com.uzuu.customer.data.remote.dto.response.TokenResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface AuthApi {

    @POST("auth/register")
    suspend fun register(
        @Body request : RegisterRequestDto
    ): BaseResponseDto<String>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): BaseResponseDto<TokenResponseDto>

    @GET("auth/verify")
    suspend fun verifyEmail(
        @Query("token") token: String
    ): BaseResponseDto<String>

}