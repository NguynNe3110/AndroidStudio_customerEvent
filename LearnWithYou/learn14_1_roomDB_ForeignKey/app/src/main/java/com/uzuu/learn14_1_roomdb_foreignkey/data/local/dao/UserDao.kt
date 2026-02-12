package com.uzuu.learn14_1_roomdb_foreignkey.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.uzuu.learn14_1_roomdb_foreignkey.data.local.entity.UserEntity
import com.uzuu.learn14_1_roomdb_foreignkey.domain.model.UserWithClass
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity): Int

    @Query("SELECT * FROM users ORDER BY id DESC")
    fun observeAll(): Flow<List<UserEntity>>

    // JOIN lấy className
    @Query("""
        SELECT u.id, u.idClass, u.nameStudent, c.name AS className
        FROM users u
        JOIN classes c ON c.id = u.idClass
        ORDER BY u.id DESC
    """)
    fun observeUsersWithClass(): Flow<List<UserWithClass>>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("DELETE FROM users")
    suspend fun deleteAll(): Int

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: Int): Int
}
