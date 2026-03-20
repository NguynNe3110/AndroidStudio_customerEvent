package com.uzuu.customer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyTicket(
    val id: Long,
    val eventName: String,
    val ticketTypeName: String,
    val ticketCode: String,
    val qrCode: String,
    val status: String,   // VALID | USED | EXPIRED
    val usedAt: String?
) : Parcelable