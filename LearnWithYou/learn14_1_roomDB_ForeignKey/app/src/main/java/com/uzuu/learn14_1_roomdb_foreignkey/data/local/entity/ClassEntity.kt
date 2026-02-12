package com.uzuu.learn14_1_roomdb_foreignkey.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classes")
data class ClassEntity(
    @PrimaryKey()
    val id: Int,
    val name: String = ""
)
