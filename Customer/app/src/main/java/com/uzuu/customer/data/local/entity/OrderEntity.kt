package com.uzuu.customer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val id: Long,
    val totalAmount: Double,
    val paymentMethod: String,
    val paymentStatus: String,
    val orderStatus: String,
    val orderDate: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "my_tickets")
data class MyTicketEntity(
    @PrimaryKey
    val id: Long,
    val eventName: String,
    val ticketTypeName: String,
    val ticketCode: String,
    val qrCode: String,
    val status: String,
    val usedAt: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)
