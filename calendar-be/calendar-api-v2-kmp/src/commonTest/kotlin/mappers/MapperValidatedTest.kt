package ru.otus.otuskotlin.calendar.api.v2.mappers

import ru.otus.otuskotlin.calendar.api.v2.models.EventCreateRequest
import ru.otus.otuskotlin.calendar.api.v2.models.EventDebug
import ru.otus.otuskotlin.calendar.api.v2.models.EventRequestDebugStubs
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.stubs.CalendarStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperValidatedTest {
    @Test
    fun fromTransportValidated() {
        val req = EventCreateRequest(
            debug = EventDebug(
                stub = EventRequestDebugStubs.SUCCESS,
            ),
        )

        val context = CalendarContext()
        context.fromTransportValidated(req)

        assertEquals(CalendarStubs.SUCCESS, context.stubCase)
    }
}
