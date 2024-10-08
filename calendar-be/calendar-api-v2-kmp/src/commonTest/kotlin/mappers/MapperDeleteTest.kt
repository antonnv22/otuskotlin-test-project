package ru.otus.otuskotlin.calendar.api.v2.mappers

import ru.otus.otuskotlin.calendar.api.v2.models.*
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.stubs.CalendarStubs
import kotlin.test.assertEquals
import kotlin.test.Test

class MapperDeleteTest {
    @Test
    fun fromTransport() {
        val req = EventDeleteRequest(
            debug = EventDebug(
                mode = EventRequestDebugMode.STUB,
                stub = EventRequestDebugStubs.SUCCESS,
            ),
            event = EventDeleteObject(
                id = "12345",
                lock = "456789",
            ),
        )

        val context = CalendarContext()
        context.fromTransport(req)

        assertEquals(CalendarStubs.SUCCESS, context.stubCase)
        assertEquals(CalendarWorkMode.STUB, context.workMode)
        assertEquals("12345", context.eventRequest.id.asString())
        assertEquals("456789", context.eventRequest.lock.asString())
    }

    @Test
    fun toTransport() {
        val context = CalendarContext(
            requestId = CalendarRequestId("1234"),
            command = CalendarCommand.DELETE,
            eventResponse = CalendarEvent(
                id = CalendarEventId("12345"),
                title = "title",
                description = "desc",
                start = "2024-08-23T19:00:00Z",
                end = "2024-08-23T19:30:00Z",
                visibility = CalendarVisibility.VISIBLE_PUBLIC,
                lock = CalendarEventLock("456789"),
            ),
            errors = mutableListOf(
                CalendarError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = CalendarState.RUNNING,
        )

        val req = context.toTransportEvent() as EventDeleteResponse

        assertEquals("12345", req.event?.id)
        assertEquals("456789", req.event?.lock)
        assertEquals("title", req.event?.title)
        assertEquals("desc", req.event?.description)
        assertEquals("2024-08-23T19:00:00Z", req.event?.start)
        assertEquals("2024-08-23T19:30:00Z", req.event?.end)
        assertEquals(EventVisibility.PUBLIC, req.event?.visibility)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
