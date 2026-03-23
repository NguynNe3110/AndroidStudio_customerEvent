package com.uzuu.admin.data.remote.api

import com.uzuu.admin.data.remote.dto.BaseResponseDto
import com.uzuu.admin.data.remote.dto.request.LoginRequestDto
import com.uzuu.admin.data.remote.dto.response.TokenResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): BaseResponseDto<TokenResponseDto>
}