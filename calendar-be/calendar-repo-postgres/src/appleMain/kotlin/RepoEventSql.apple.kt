package ru.otus.otuskotlin.calendar.backend.repo.postgresql

import ru.otus.otuskotlin.calendar.common.models.CalendarEvent
import ru.otus.otuskotlin.calendar.common.repo.*
import ru.otus.otuskotlin.calendar.repo.common.IRepoEventInitializable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoEventSql actual constructor(
    properties: SqlProperties,
    randomUuid: () -> String
) : IRepoEvent, IRepoEventInitializable {
    actual override fun save(events: Collection<CalendarEvent>): Collection<CalendarEvent> {
        TODO("Not yet implemented")
    }

    actual override suspend fun createEvent(rq: DbEventRequest): IDbEventResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun readEvent(rq: DbEventIdRequest): IDbEventResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun updateEvent(rq: DbEventRequest): IDbEventResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun deleteEvent(rq: DbEventIdRequest): IDbEventResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun searchEvent(rq: DbEventFilterRequest): IDbEventsResponse {
        TODO("Not yet implemented")
    }

    actual fun clear() {
        TODO("Not yet implemented")
    }
}
