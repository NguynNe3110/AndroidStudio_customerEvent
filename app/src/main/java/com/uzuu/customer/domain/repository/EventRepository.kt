package com.uzuu.customer.domain.repository

import com.uzuu.customer.domain.model.Event
import com.uzuu.customer.domain.model.PagedResult

interface EventRepository {
    suspend fun getEvent(page: Int): PagedResult<Event>
}


