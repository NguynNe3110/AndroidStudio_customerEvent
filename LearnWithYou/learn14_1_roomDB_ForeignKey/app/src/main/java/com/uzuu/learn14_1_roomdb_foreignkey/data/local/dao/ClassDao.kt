package com.uzuu.learn14_1_roomdb_foreignkey.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.uzuu.learn14_1_roomdb_foreignkey.data.local.entity.ClassEntity
import com.uzuu.learn14_1_roomdb_foreignkey.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(classes: ClassEntity): Long

    @Update
    suspend fun update(classes: ClassEntity): Int

    @Query("SELECT * FROM classes ORDER BY id DESC")
    fun observeAll(): Flow<List<ClassEntity>>

    @Query("SELECT * FROM classes WHERE id = :id LIMIT 1")
    suspend fun getClassById(id: Int): ClassEntity?

    @Query("DELETE FROM classes")
    suspend fun deleteAll(): Int

    @Query("DELETE FROM classes WHERE id = :id")
    suspend fun deleteClassById(id: Int): Int
}
