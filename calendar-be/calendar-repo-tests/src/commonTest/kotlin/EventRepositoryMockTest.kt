package ru.otus.otuskotlin.calendar.backend.repo.tests

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.calendar.common.models.CalendarEvent
import ru.otus.otuskotlin.calendar.common.repo.*
import ru.otus.otuskotlin.calendar.stubs.CalendarEventStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class EventRepositoryMockTest {
    private val repo = EventRepositoryMock(
        invokeCreateEvent = { DbEventResponseOk(CalendarEventStub.prepareResult { title = "create" }) },
        invokeReadEvent = { DbEventResponseOk(CalendarEventStub.prepareResult { title = "read" }) },
        invokeUpdateEvent = { DbEventResponseOk(CalendarEventStub.prepareResult { title = "update" }) },
        invokeDeleteEvent = { DbEventResponseOk(CalendarEventStub.prepareResult { title = "delete" }) },
        invokeSearchEvent = { DbEventsResponseOk(listOf(CalendarEventStub.prepareResult { title = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createEvent(DbEventRequest(CalendarEvent()))
        assertIs<DbEventResponseOk>(result)
        assertEquals("create", result.data.title)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readEvent(DbEventIdRequest(CalendarEvent()))
        assertIs<DbEventResponseOk>(result)
        assertEquals("read", result.data.title)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateEvent(DbEventRequest(CalendarEvent()))
        assertIs<DbEventResponseOk>(result)
        assertEquals("update", result.data.title)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteEvent(DbEventIdRequest(CalendarEvent()))
        assertIs<DbEventResponseOk>(result)
        assertEquals("delete", result.data.title)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchEvent(DbEventFilterRequest())
        assertIs<DbEventsResponseOk>(result)
        assertEquals("search", result.data.first().title)
    }
}
