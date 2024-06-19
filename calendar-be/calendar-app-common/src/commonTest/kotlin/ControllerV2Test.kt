package ru.otus.otuskotlin.calendar.app.common

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.calendar.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.calendar.api.v2.mappers.toTransportEvent
import ru.otus.otuskotlin.calendar.api.v2.models.*
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV2Test {

    private val request = EventCreateRequest(
        event = EventCreateObject(
            title = "some event",
            description = "some description of some event",
            visibility = EventVisibility.PUBLIC,
        ),
        debug = EventDebug(mode = EventRequestDebugMode.STUB, stub = EventRequestDebugStubs.SUCCESS)
    )

    private val appSettings: ICalendarAppSettings = object : ICalendarAppSettings {
        override val corSettings: CalendarCorSettings = CalendarCorSettings()
        override val processor: CalendarEventProcessor = CalendarEventProcessor(corSettings)
    }

    private suspend fun createEventSpring(request: EventCreateRequest): EventCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportEvent() as EventCreateResponse },
            ControllerV2Test::class,
            "controller-v2-test"
        )

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createEventKtor(appSettings: ICalendarAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<EventCreateRequest>()) },
            { toTransportEvent() },
            ControllerV2Test::class,
            "controller-v2-test"
        )
        respond(resp)
    }

    @Test
    fun springHelperTest() = runTest {
        val res = createEventSpring(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createEventKtor(appSettings) }
        val res = testApp.res as EventCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}
