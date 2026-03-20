package com.uzuu.customer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
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
    val imageUrls: List<String>,
    val ticketTypes: List<CategoryTicket>,
) : Parcelable