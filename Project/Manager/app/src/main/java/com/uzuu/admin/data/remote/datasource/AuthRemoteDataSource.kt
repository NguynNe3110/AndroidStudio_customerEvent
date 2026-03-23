package com.uzuu.admin.data.remote.datasource

import com.uzuu.admin.data.remote.api.AuthApi
import com.uzuu.admin.data.remote.dto.BaseResponseDto
import com.uzuu.admin.data.remote.dto.request.LoginRequestDto
import com.uzuu.admin.data.remote.dto.response.TokenResponseDto
import retrofit2.http.Body

class AuthRemoteDataSource(
    private val api: AuthApi
) {
    suspend fun login(request: LoginRequestDto ): BaseResponseDto<TokenResponseDto> {
        return api.login(request)
    }
}