package com.uzuu.customer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryTicket(
    val id: Long,
    val name: String,
    val price: Double,
    val totalQuantity: Int,
    val remainingQuantity: Int
) : Parcelable