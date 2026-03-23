package com.uzuu.admin.data.repository

import com.uzuu.admin.core.result.ApiResult
import com.uzuu.admin.core.result.safeApiCall
import com.uzuu.admin.data.remote.datasource.AuthRemoteDataSource
import com.uzuu.admin.data.remote.datasource.CheckInRemoteDataSource
import com.uzuu.admin.data.remote.dto.request.LoginRequestDto
import com.uzuu.admin.domain.model.CheckInResult
import com.uzuu.admin.domain.model.Login
import com.uzuu.admin.domain.repository.AuthRepository
import com.uzuu.admin.domain.repository.CheckInRepository

class AuthRepositoryImpl(
    private val remote: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun login(request: Login): ApiResult<String> =
        safeApiCall {
            val response = remote.login(LoginRequestDto(request.username, request.password))
            if (response.code == 200 || response.code == 0 || response.code == 1000) {
                response.result.token
            } else {
                throw Exception(response.message ?: "Đăng nhập thất bại")
            }
        }
}

class CheckInRepositoryImpl(
    private val remote: CheckInRemoteDataSource
) : CheckInRepository {

    override suspend fun checkIn(ticketCode: String): ApiResult<CheckInResult> =
        safeApiCall {
            val response = remote.checkIn(ticketCode)
            if (response.code == 200 || response.code == 0 || response.code == 1000) {
                val dto = response.result
                CheckInResult(
                    id             = dto.id,
                    eventName      = dto.eventName,
                    ticketTypeName = dto.ticketTypeName,
                    ticketCode     = dto.ticketCode,
                    status         = dto.status,
                    usedAt         = dto.usedAt
                )
            } else {
                throw Exception(response.message ?: "Check-in thất bại")
            }
        }
}