package com.uzuu.customer.data.repository

import com.uzuu.customer.data.mapper.eventDtoToDomain
import com.uzuu.customer.data.remote.datasource.EventRemoteDataSource
import com.uzuu.customer.domain.model.Event
import com.uzuu.customer.domain.model.PagedResult
import com.uzuu.customer.domain.repository.EventRepository

class EventRepositoryImpl(
    private val eventRemote: EventRemoteDataSource
) : EventRepository {

    override suspend fun getEvent(page: Int): PagedResult<Event> {
        println("DEBUG [EventRepositoryImpl] getEvent(page=$page) called")

        val response = eventRemote.getEvent(page)
        println("DEBUG [EventRepositoryImpl] raw response — code=${response.code}, message='${response.message}'")


        val pageData = response.result
        println("DEBUG [EventRepositoryImpl] result — content=${pageData.content.size}, totalPages=${pageData.totalPages}, totalElements=${pageData.totalElements}, isLast=${pageData.last}, currentPage=${pageData.number}")

        if (pageData.content.isEmpty()) {
            println("DEBUG [EventRepositoryImpl] WARNING: content is empty!")
        }

        val mapped = pageData.content.map {
            println("DEBUG [EventRepositoryImpl]   mapping event id=${it.id}, name='${it.name}', status='${it.status}', ticketTypes=${it.ticketTypes.size}")
            it.eventDtoToDomain()
        }

        return PagedResult(
            data = mapped,
            page = pageData.number,
            totalPages = pageData.totalPages,
            totalElements = pageData.totalElements,
            isLast = pageData.last
        )
    }
}