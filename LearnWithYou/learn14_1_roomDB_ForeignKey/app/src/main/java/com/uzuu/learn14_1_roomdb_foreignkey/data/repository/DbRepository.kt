package com.uzuu.learn14_1_roomdb_foreignkey.data.repository

import com.uzuu.learn14_1_roomdb_foreignkey.data.local.dao.ClassDao
import com.uzuu.learn14_1_roomdb_foreignkey.data.local.dao.UserDao
import com.uzuu.learn14_1_roomdb_foreignkey.data.local.entity.ClassEntity
import com.uzuu.learn14_1_roomdb_foreignkey.data.local.entity.UserEntity
import com.uzuu.learn14_1_roomdb_foreignkey.domain.model.User
import com.uzuu.learn14_1_roomdb_foreignkey.domain.model.UserWithClass
import kotlinx.coroutines.flow.Flow

class DbRepository(
    private val classDao: ClassDao,
    private val userDao: UserDao
) {
    fun observeClasses(): Flow<List<ClassEntity>> = classDao.observeAll()

    //dùng JOIN flow
    fun observeUsersWithClass(): Flow<List<UserWithClass>> = userDao.observeUsersWithClass()

    suspend fun addUser(idUser: Int, idClass: Int, userName: String): Long {
        return userDao.insert(UserEntity(id = idUser, idClass = idClass, nameStudent = userName))
    }

    suspend fun updUser(idUser: Int, idClass: Int, userName: String): Int {
        return userDao.update(UserEntity(id = idUser, idClass = idClass, nameStudent = userName))
    }

    suspend fun deleteUserById(id: Int): Int = userDao.deleteUserById(id)

    suspend fun addClass(id: Int, className: String): Long {
        return classDao.insert(ClassEntity(id = id, name = className))
    }

    suspend fun updClass(id: Int, className: String): Int {
        return classDao.update(ClassEntity(id = id, name = className))
    }

    suspend fun deleteClassById(id: Int): Int = classDao.deleteClassById(id)

    suspend fun clearAll() {
        userDao.deleteAll()
        classDao.deleteAll()
    }
}
