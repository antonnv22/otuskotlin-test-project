package ru.otus.otuskotlin.calendar.biz.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.stubs.CalendarStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class EventUpdateStubTest {

    private val processor = CalendarEventProcessor()
    private val id = CalendarEventId("0002")
    val title = "груминг"
    private val description = "груминг задач в начале очередного спринта"
    private val visibility = CalendarVisibility.VISIBLE_PUBLIC
    private val start = "2024-08-23T19:00:00Z"
    private val end = "2024-08-23T19:30:00Z"

    @Test
    fun create() = runTest {

        val ctx = CalendarContext(
            command = CalendarCommand.UPDATE,
            state = CalendarState.NONE,
            workMode = CalendarWorkMode.STUB,
            stubCase = CalendarStubs.SUCCESS,
            eventRequest = CalendarEvent(
                id = id,
                title = title,
                description = description,
                start = start,
                end = end,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.eventResponse.id)
        assertEquals(title, ctx.eventResponse.title)
        assertEquals(description, ctx.eventResponse.description)
        assertEquals(visibility, ctx.eventResponse.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = CalendarContext(
            command = CalendarCommand.UPDATE,
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
    fun badTitle() = runTest {
        val ctx = CalendarContext(
            command = CalendarCommand.UPDATE,
            state = CalendarState.NONE,
            workMode = CalendarWorkMode.STUB,
            stubCase = CalendarStubs.BAD_TITLE,
            eventRequest = CalendarEvent(
                id = id,
                title = "",
                description = description,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(CalendarEvent(), ctx.eventResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val ctx = CalendarContext(
            command = CalendarCommand.UPDATE,
            state = CalendarState.NONE,
            workMode = CalendarWorkMode.STUB,
            stubCase = CalendarStubs.BAD_DESCRIPTION,
            eventRequest = CalendarEvent(
                id = id,
                title = title,
                description = "",
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(CalendarEvent(), ctx.eventResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = CalendarContext(
            command = CalendarCommand.UPDATE,
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
            command = CalendarCommand.UPDATE,
            state = CalendarState.NONE,
            workMode = CalendarWorkMode.STUB,
            stubCase = CalendarStubs.BAD_SEARCH_STRING,
            eventRequest = CalendarEvent(
                id = id,
                title = title,
                description = description,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(CalendarEvent(), ctx.eventResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
