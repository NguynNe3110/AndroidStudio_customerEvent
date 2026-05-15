package com.uzuu.learn12_2_restfulapi_basic_real.data.remote.datasource

import com.uzuu.learn12_2_restfulapi_basic_real.data.remote.api.UserApi
import com.uzuu.learn12_2_restfulapi_basic_real.data.remote.dto.BaseResponseDto
import com.uzuu.learn12_2_restfulapi_basic_real.data.remote.dto.user.CreateUserRequest
import com.uzuu.learn12_2_restfulapi_basic_real.data.remote.dto.user.UpdateUserRequest
import com.uzuu.learn12_2_restfulapi_basic_real.data.remote.dto.user.UserDto

class UserRemoteDataSource(
    private val userApi: UserApi
) {

    suspend fun createUser(request: CreateUserRequest): BaseResponseDto<UserDto> {
        return userApi.createUser(request)
    }

    suspend fun deleteUser(username: String) {
        return userApi.deleteUser(username)
    }

    suspend fun getAllUsers(): BaseResponseDto<List<UserDto>> {
        return userApi.getAllUsers()
    }

    suspend fun updateUser(
        id: Int,
        request: UpdateUserRequest
    ): BaseResponseDto<UserDto> {
        return userApi.updateUser(id, request)
    }

    suspend fun updateFullUser(
        username: String,
        request: UpdateUserRequest
    ): BaseResponseDto<UserDto> {
        return userApi.updateFullUser(username, request)
    }
}