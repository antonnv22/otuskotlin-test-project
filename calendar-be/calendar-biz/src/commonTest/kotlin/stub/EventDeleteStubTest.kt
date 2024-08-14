package ru.otus.otuskotlin.calendar.biz.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.stubs.CalendarStubs
import ru.otus.otuskotlin.calendar.stubs.CalendarEventStub
import kotlin.test.Test
import kotlin.test.assertEquals

class EventDeleteStubTest {

    private val processor = CalendarEventProcessor()
    val id = CalendarEventId("0001")

    @Test
    fun delete() = runTest {

        val ctx = CalendarContext(
            command = CalendarCommand.DELETE,
            state = CalendarState.NONE,
            workMode = CalendarWorkMode.STUB,
            stubCase = CalendarStubs.SUCCESS,
            eventRequest = CalendarEvent(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = CalendarEventStub.get()
        assertEquals(stub.id, ctx.eventResponse.id)
        assertEquals(stub.title, ctx.eventResponse.title)
        assertEquals(stub.description, ctx.eventResponse.description)
        assertEquals(stub.visibility, ctx.eventResponse.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = CalendarContext(
            command = CalendarCommand.DELETE,
            state = CalendarState.NONE,
            workMode = CalendarWorkMode.STUB,
            stubCase = CalendarStubs.BAD_ID,
            eventRequest = CalendarEvent(),
        )
        processor.exec(ctx)
        assertEquals(CalendarEvent(), ctx.eventResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = CalendarContext(
            command = CalendarCommand.DELETE,
            state = CalendarState.NONE,
            workMode = CalendarWorkMode.STUB,
            stubCase = CalendarStubs.DB_ERROR,
            eventRequest = CalendarEvent(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(CalendarEvent(), ctx.eventResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = CalendarContext(
            command = CalendarCommand.DELETE,
            state = CalendarState.NONE,
            workMode = CalendarWorkMode.STUB,
            stubCase = CalendarStubs.BAD_TITLE,
            eventRequest = CalendarEvent(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(CalendarEvent(), ctx.eventResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
