package com.uzuu.customer.data.repository

import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.core.result.safeApiCall
import com.uzuu.customer.data.mapper.loginDomainToDto
import com.uzuu.customer.data.mapper.registerDomainToDto
import com.uzuu.customer.data.remote.datasource.AuthRemoteDataSource
import com.uzuu.customer.data.remote.dto.BaseResponseDto
import com.uzuu.customer.data.remote.dto.response.TokenResponseDto
import com.uzuu.customer.domain.model.Login
import com.uzuu.customer.domain.model.Register
import com.uzuu.customer.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remote: AuthRemoteDataSource
): AuthRepository {
    override suspend fun registerRequest(request: Register): ApiResult<String> =
        safeApiCall {
            val response = remote.register(request.registerDomainToDto())
            // Kiểm tra code thực tế server trả về (200, 201, 0, 1000...)
            if (response.code == 200 || response.code == 0 || response.code == 1000) {
                response.result ?: "Đăng ký thành công"
            } else {
                throw Exception(response.message ?: "Đăng ký thất bại")
            }
        }

    override suspend fun loginRequest(request: Login): ApiResult<BaseResponseDto<TokenResponseDto>> =
        safeApiCall {
            println("DEBUG 1232 impl(): ")

            remote.login(request.loginDomainToDto())
        }
}