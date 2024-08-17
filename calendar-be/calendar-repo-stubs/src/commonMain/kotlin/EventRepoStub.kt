package ru.otus.otuskotlin.calendar.backend.repository.inmemory

import ru.otus.otuskotlin.calendar.common.repo.*
import ru.otus.otuskotlin.calendar.stubs.CalendarEventStub

class EventRepoStub() : IRepoEvent {
    override suspend fun createEvent(rq: DbEventRequest): IDbEventResponse {
        return DbEventResponseOk(
            data = CalendarEventStub.get(),
        )
    }

    override suspend fun readEvent(rq: DbEventIdRequest): IDbEventResponse {
        return DbEventResponseOk(
            data = CalendarEventStub.get(),
        )
    }

    override suspend fun updateEvent(rq: DbEventRequest): IDbEventResponse {
        return DbEventResponseOk(
            data = CalendarEventStub.get(),
        )
    }

    override suspend fun deleteEvent(rq: DbEventIdRequest): IDbEventResponse {
        return DbEventResponseOk(
            data = CalendarEventStub.get(),
        )
    }

    override suspend fun searchEvent(rq: DbEventFilterRequest): IDbEventsResponse {
        return DbEventsResponseOk(
            data = CalendarEventStub.prepareSearchList(filter = ""),
        )
    }
}
