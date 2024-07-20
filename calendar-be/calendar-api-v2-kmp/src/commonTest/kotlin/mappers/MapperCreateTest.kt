package ru.otus.otuskotlin.calendar.api.v2.mappers

import ru.otus.otuskotlin.calendar.api.v2.models.*
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.stubs.CalendarStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperCreateTest {
    @Test
    fun fromTransport() {
        val req = EventCreateRequest(
            debug = EventDebug(
                mode = EventRequestDebugMode.STUB,
                stub = EventRequestDebugStubs.SUCCESS,
            ),
            event = EventCreateObject(
                title = "title",
                description = "desc",
                visibility = EventVisibility.PUBLIC,
            ),
        )

        val context = CalendarContext()
        context.fromTransport(req)

        assertEquals(CalendarStubs.SUCCESS, context.stubCase)
        assertEquals(CalendarWorkMode.STUB, context.workMode)
        assertEquals("title", context.eventRequest.title)
        assertEquals(CalendarVisibility.VISIBLE_PUBLIC, context.eventRequest.visibility)
    }

    @Test
    fun toTransport() {
        val context = CalendarContext(
            requestId = CalendarRequestId("1234"),
            command = CalendarCommand.CREATE,
            eventResponse = CalendarEvent(
                title = "title",
                description = "desc",
                visibility = CalendarVisibility.VISIBLE_PUBLIC,
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

        val req = context.toTransportEvent() as EventCreateResponse

        assertEquals("title", req.event?.title)
        assertEquals("desc", req.event?.description)
        assertEquals(EventVisibility.PUBLIC, req.event?.visibility)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
