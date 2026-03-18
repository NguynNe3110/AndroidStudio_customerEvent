package com.uzuu.customer.domain.repository

import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.data.remote.dto.BaseResponseDto
import com.uzuu.customer.data.remote.dto.response.TokenResponseDto
import com.uzuu.customer.domain.model.Login
import com.uzuu.customer.domain.model.Register

interface AuthRepository {

    suspend fun registerRequest(request: Register): ApiResult<String>

    suspend fun loginRequest(request: Login): ApiResult<BaseResponseDto<TokenResponseDto>>


}