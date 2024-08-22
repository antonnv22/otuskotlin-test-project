package ru.otus.otuskotlin.calendar.backend.repo.tests

import ru.otus.otuskotlin.calendar.common.models.CalendarEvent
import ru.otus.otuskotlin.calendar.common.repo.*

class EventRepositoryMock(
    private val invokeCreateEvent: (DbEventRequest) -> IDbEventResponse = { DEFAULT_EVENT_SUCCESS_EMPTY_MOCK },
    private val invokeReadEvent: (DbEventIdRequest) -> IDbEventResponse = { DEFAULT_EVENT_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateEvent: (DbEventRequest) -> IDbEventResponse = { DEFAULT_EVENT_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteEvent: (DbEventIdRequest) -> IDbEventResponse = { DEFAULT_EVENT_SUCCESS_EMPTY_MOCK },
    private val invokeSearchEvent: (DbEventFilterRequest) -> IDbEventsResponse = { DEFAULT_EVENTS_SUCCESS_EMPTY_MOCK },
): IRepoEvent {
    override suspend fun createEvent(rq: DbEventRequest): IDbEventResponse {
        return invokeCreateEvent(rq)
    }

    override suspend fun readEvent(rq: DbEventIdRequest): IDbEventResponse {
        return invokeReadEvent(rq)
    }

    override suspend fun updateEvent(rq: DbEventRequest): IDbEventResponse {
        return invokeUpdateEvent(rq)
    }

    override suspend fun deleteEvent(rq: DbEventIdRequest): IDbEventResponse {
        return invokeDeleteEvent(rq)
    }

    override suspend fun searchEvent(rq: DbEventFilterRequest): IDbEventsResponse {
        return invokeSearchEvent(rq)
    }

    companion object {
        val DEFAULT_EVENT_SUCCESS_EMPTY_MOCK = DbEventResponseOk(CalendarEvent())
        val DEFAULT_EVENTS_SUCCESS_EMPTY_MOCK = DbEventsResponseOk(emptyList())
    }
}
