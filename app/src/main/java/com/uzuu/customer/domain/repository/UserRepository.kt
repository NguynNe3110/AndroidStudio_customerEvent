package com.uzuu.customer.domain.repository

import com.uzuu.customer.data.local.entity.UsersEntity
import com.uzuu.customer.domain.model.Users
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val users : Flow<List<Users>>

    suspend fun createUser(user: Users): Long

    suspend fun updateUser(user: Users) : Int

    suspend fun deleteUserById(id: Int) : Int

    suspend fun getUserById(id: Int) : Users

    suspend fun isUserExist(username: String): Boolean

    suspend fun login(username: String, password: String): UsersEntity?
}