package com.uzuu.customer.data.repository

import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.core.result.safeApiCall
import com.uzuu.customer.data.local.datasource.UserDataLocalSource
import com.uzuu.customer.data.mapper.userdomainToDto
import com.uzuu.customer.data.remote.datasource.UserRemoteDataSource
import com.uzuu.customer.data.remote.dto.request.UserRequestDto
import com.uzuu.customer.data.remote.dto.response.UserResponseDto
import com.uzuu.customer.domain.model.Users
import com.uzuu.customer.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userLocal: UserDataLocalSource,
    private val userRemote: UserRemoteDataSource
) : UserRepository {

    // ── Remote ───────────────────────────────────────────────────────────────
    override suspend fun getMyInfo(): ApiResult<UserResponseDto> =
        safeApiCall {
            val response = userRemote.getMyInfo()
            if (response.code == 200 || response.code == 0 || response.code == 1000) {
                response.result
            } else {
                throw Exception(response.message ?: "Không lấy được thông tin")
            }
        }

    override suspend fun updateInfo(
        username: String,
        request: UserRequestDto
    ): ApiResult<UserResponseDto> =
        safeApiCall {
            val response = userRemote.updateInfo(username, request)
            if (response.code == 200 || response.code == 0 || response.code == 1000) {
                response.result
            } else {
                throw Exception(response.message ?: "Cập nhật thất bại")
            }
        }

    // ── Local (Room) ──────────────────────────────────────────────────────────
    override val users: Flow<List<Users>>
        get() = TODO("Not yet implemented")

    override suspend fun createUser(user: Users): Long {
        return userLocal.createUser(user.userdomainToDto())
    }

    override suspend fun updateUser(user: Users): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(id: Int): Users {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUserById(id: Int): Int {
        TODO("Not yet implemented")
    }

    override suspend fun isUserExist(username: String): Boolean {
        TODO("Not yet implemented")
    }
}