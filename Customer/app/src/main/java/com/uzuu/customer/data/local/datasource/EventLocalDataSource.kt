package com.uzuu.customer.data.local.datasource

import com.uzuu.customer.data.local.dao.EventDao
import com.uzuu.customer.data.local.dao.TicketDao
import com.uzuu.customer.data.local.entity.EventEntity
import com.uzuu.customer.data.local.entity.TicketEntity
import com.uzuu.customer.data.mapper.toEntity
import com.uzuu.customer.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EventLocalDataSource(
    private val eventDao: EventDao,
    private val ticketDao: TicketDao
) {
    fun observeAllEvents(): Flow<List<Event>> {
        return eventDao.observeAllEvents().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getEventById(id: Long): Event? {
        return eventDao.getEventById(id)?.toDomain()
    }

    suspend fun cacheEvents(events: List<Event>) {
        val eventEntities = events.map { it.toEntity() }
        val ticketEntities = events.flatMap { event ->
            event.ticketTypes.map { ticket ->
                TicketEntity(
                    id = ticket.id,
                    eventId = event.id,
                    categoryTicketId = ticket.id,
                    name = ticket.name,
                    price = ticket.price,
                    totalQuantity = ticket.totalQuantity,
                    remainingQuantity = ticket.remainingQuantity
                )
            }
        }
        eventDao.insertEvents(eventEntities)
        ticketDao.insertTickets(ticketEntities)
    }

    suspend fun clearCache() {
        eventDao.deleteAllEvents()
        ticketDao.deleteAllTickets()
    }

    suspend fun hasData(): Boolean {
        return eventDao.getEventCount() > 0
    }
}

private fun EventEntity.toDomain(): Event {
    return Event(
        id = id,
        name = name,
        categoryName = categoryName,
        location = location,
        startTime = startTime,
        endTime = endTime,
        saleStartDate = saleStartDate,
        saleEndDate = saleEndDate,
        description = description,
        status = status,
        imageUrls = try {
            org.json.JSONArray(imageUrls).let { json ->
                List(json.length()) { i -> json.getString(i) }
            }
        } catch (e: Exception) {
            emptyList()
        },
        ticketTypes = emptyList() // Will be loaded separately if needed
    )
}

private fun Event.toEntity(): EventEntity {
    return EventEntity(
        id = id,
        name = name,
        categoryName = categoryName,
        location = location,
        startTime = startTime,
        endTime = endTime,
        saleStartDate = saleStartDate,
        saleEndDate = saleEndDate,
        description = description,
        status = status,
        imageUrls = try {
            org.json.JSONArray(imageUrls).toString()
        } catch (e: Exception) {
            "[]"
        }
    )
}
