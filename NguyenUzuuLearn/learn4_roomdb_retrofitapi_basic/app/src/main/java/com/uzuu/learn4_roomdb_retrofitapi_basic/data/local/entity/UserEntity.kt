package com.uzuu.learn4_roomdb_retrofitapi_basic.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String
)
