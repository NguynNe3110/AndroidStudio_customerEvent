package com.uzuu.customer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey
    val id: Long,
    val ticketTypeId: Long,
    val ticketTypeName: String,
    val eventName: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey
    val id: Long,
    val totalAmount: Double,
    val lastUpdated: Long = System.currentTimeMillis()
)