package com.uzuu.customer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val categoryName: String,
    val location: String,
    val startTime: String?,
    val endTime: String?,
    val saleStartDate: String?,
    val saleEndDate: String?,
    val description: String?,
    val status: String,
    val imageUrls: String, // Store as JSON string
    val lastUpdated: Long = System.currentTimeMillis()
)
