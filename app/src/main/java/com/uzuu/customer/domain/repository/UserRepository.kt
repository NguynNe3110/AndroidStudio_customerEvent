package com.uzuu.customer.domain.repository

import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.data.remote.dto.request.UserRequestDto
import com.uzuu.customer.data.remote.dto.response.UserResponseDto
import com.uzuu.customer.domain.model.Users
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    // ── Remote ───────────────────────────────────────────────────────────────
    suspend fun getMyInfo(): ApiResult<UserResponseDto>

    suspend fun updateInfo(username: String, request: UserRequestDto): ApiResult<UserResponseDto>

    // ── Local (Room) ──────────────────────────────────────────────────────────
    val users: Flow<List<Users>>

    suspend fun createUser(user: Users): Long

    suspend fun updateUser(user: Users): Int

    suspend fun deleteUserById(id: Int): Int

    suspend fun getUserById(id: Int): Users

    suspend fun isUserExist(username: String): Boolean
}