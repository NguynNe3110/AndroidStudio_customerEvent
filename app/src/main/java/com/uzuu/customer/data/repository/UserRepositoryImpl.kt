package com.uzuu.customer.data.repository

import com.uzuu.customer.data.local.datasource.UserDataLocalSource
import com.uzuu.customer.data.local.entity.UsersEntity
import com.uzuu.customer.data.mapper.userdomainToDto
import com.uzuu.customer.domain.model.Users
import com.uzuu.customer.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userLocal : UserDataLocalSource
): UserRepository {
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

    override suspend fun login(username: String, password: String): UsersEntity? {
        return userLocal.login(username,password)
    }
}