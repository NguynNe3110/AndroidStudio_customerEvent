package com.uzuu.customer.feature.middle.home

sealed class UiItemHome {
    data class upComing(
        val id: Int
    )

    data class onSale(
        val id: Int
    )

    data class onGoing(
        val id: Int
    )

    data class completed(
        val id: Int
    )
}