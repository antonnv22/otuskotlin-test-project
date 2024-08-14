package ru.otus.otuskotlin.calendar.biz.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.stubs.CalendarStubs
import ru.otus.otuskotlin.calendar.stubs.CalendarEventStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class EventSearchStubTest {

    private val processor = CalendarEventProcessor()
    private val filter = CalendarEventFilter(searchString = "daily")

    @Test
    fun read() = runTest {

        val ctx = CalendarContext(
            command = CalendarCommand.SEARCH,
            state = CalendarState.NONE,
            workMode = CalendarWorkMode.STUB,
            stubCase = CalendarStubs.SUCCESS,
            eventFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.eventsResponse.size > 1)
        val first = ctx.eventsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(filter.searchString))
        assertTrue(first.description.contains(filter.searchString))
        with (CalendarEventStub.get()) {
            assertEquals(visibility, first.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = CalendarContext(
            command = CalendarCommand.SEARCH,
            state = CalendarState.NONE,
            workMode = CalendarWorkMode.STUB,
            stubCase = CalendarStubs.BAD_ID,
            eventFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(CalendarEvent(), ctx.eventResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = CalendarContext(
            command = CalendarCommand.SEARCH,
            state = CalendarState.NONE,
            workMode = CalendarWorkMode.STUB,
            stubCase = CalendarStubs.DB_ERROR,
            eventFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(CalendarEvent(), ctx.eventResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = CalendarContext(
            command = CalendarCommand.SEARCH,
            state = CalendarState.NONE,
            workMode = CalendarWorkMode.STUB,
            stubCase = CalendarStubs.BAD_TITLE,
            eventFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(CalendarEvent(), ctx.eventResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
