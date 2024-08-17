package ru.otus.otuskotlin.calendar.backend.repo.postgresql

import com.benasher44.uuid.uuid4
import ru.otus.otuskotlin.calendar.common.models.CalendarEvent
import ru.otus.otuskotlin.calendar.common.repo.*
import ru.otus.otuskotlin.calendar.repo.common.IRepoEventInitializable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class RepoEventSql(
    properties: SqlProperties,
    randomUuid: () -> String = { uuid4().toString() },
) : IRepoEvent, IRepoEventInitializable {
    override fun save(events: Collection<CalendarEvent>): Collection<CalendarEvent>
    override suspend fun createEvent(rq: DbEventRequest): IDbEventResponse
    override suspend fun readEvent(rq: DbEventIdRequest): IDbEventResponse
    override suspend fun updateEvent(rq: DbEventRequest): IDbEventResponse
    override suspend fun deleteEvent(rq: DbEventIdRequest): IDbEventResponse
    override suspend fun searchEvent(rq: DbEventFilterRequest): IDbEventsResponse
    fun clear()
}
