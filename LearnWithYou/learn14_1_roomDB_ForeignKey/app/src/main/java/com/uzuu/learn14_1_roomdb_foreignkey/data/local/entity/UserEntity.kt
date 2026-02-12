package com.uzuu.learn14_1_roomdb_foreignkey.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users",
    foreignKeys = [
        ForeignKey(
            ClassEntity::class,
            parentColumns = ["id"],
            childColumns = ["idClass"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["idClass"])
    ]
)
data class UserEntity(
    @PrimaryKey()
    val id: Int,
    val idClass: Int, //khoa ngoai
    val nameStudent: String = ""
)
