package com.uzuu.customer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey
    val id: Long,
    val eventId: Long,
    val categoryTicketId: Long,
    val name: String,
    val price: Double,
    val totalQuantity: Int,
    val remainingQuantity: Int,
    val lastUpdated: Long = System.currentTimeMillis()
)