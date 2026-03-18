package com.uzuu.customer.data.local.datasource

import com.uzuu.customer.data.local.dao.UsersDao
import com.uzuu.customer.data.local.entity.UsersEntity


class UserDataLocalSource(
    private val usersDao: UsersDao
) {
    fun observeUser() = usersDao.observeUser()

    suspend fun createUser(user: UsersEntity): Long {
        return usersDao.createUser(user)
    }

    suspend fun updateUser(user: UsersEntity) : Int {
        return usersDao.updateUser(user)
    }

    suspend fun deleteUserById(id: Int) : Int {
        return usersDao.deleteUserById(id)
    }

    suspend fun getUserById(id: Int) : UsersEntity? {
        return usersDao.getUserById(id)
    }

    suspend fun isUserExist(username: String): Boolean {
        return usersDao.isUserExist(username)
    }

    suspend fun login(username: String, password: String): UsersEntity? {
        return usersDao.login(username, password)
    }
}