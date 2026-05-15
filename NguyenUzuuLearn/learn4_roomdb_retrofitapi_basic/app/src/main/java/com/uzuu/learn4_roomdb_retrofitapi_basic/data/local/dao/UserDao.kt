package com.uzuu.learn4_roomdb_retrofitapi_basic.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uzuu.learn4_roomdb_retrofitapi_basic.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {
    @Query("select * from users order by id asc")
    fun observeAll() : Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<UserEntity>)

    @Query("delete from users")
    suspend fun clearAll()
}