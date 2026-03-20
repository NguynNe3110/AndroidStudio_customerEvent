package com.uzuu.customer.data.remote.datasource

import com.uzuu.customer.data.remote.api.UserApi
import com.uzuu.customer.data.remote.dto.BaseResponseDto
import com.uzuu.customer.data.remote.dto.request.UserRequestDto
import com.uzuu.customer.data.remote.dto.response.UserResponseDto

class UserRemoteDataSource(
    private val userApi: UserApi
) {
    suspend fun getMyInfo(): BaseResponseDto<UserResponseDto> {
        return userApi.getMyInfo()
    }

    suspend fun getInfoByUsername(username: String): BaseResponseDto<UserResponseDto> {
        return userApi.getInfoByUsername(username)
    }

    suspend fun updateInfo(username: String, request: UserRequestDto): BaseResponseDto<UserResponseDto> {
        return userApi.updateInfo(username, request)
    }
}