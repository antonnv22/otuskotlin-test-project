package ru.otus.otuskotlin.calendar.app.spring.repo

import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.calendar.api.v2.mappers.*
import ru.otus.otuskotlin.calendar.api.v2.models.*
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.stubs.CalendarEventStub
import kotlin.test.Test

internal abstract class EventRepoBaseV2Test {
    protected abstract var webClient: WebTestClient
    private val debug = EventDebug(mode = EventRequestDebugMode.TEST)
    protected val uuidNew = "10000000-0000-0000-0000-000000000001"

    @Test
    open fun createEvent() = testRepoEvent(
        "create",
        EventCreateRequest(
            event = CalendarEventStub.get().toTransportCreate(),
            debug = debug,
        ),
        prepareCtx(CalendarEventStub.prepareResult {
            id = CalendarEventId(uuidNew)
            lock = CalendarEventLock(uuidNew)
        })
            .toTransportCreate()
    )

    @Test
    open fun readEvent() = testRepoEvent(
        "read",
        EventReadRequest(
            event = CalendarEventStub.get().toTransportRead(),
            debug = debug,
        ),
        prepareCtx(CalendarEventStub.get())
            .toTransportRead()
    )

    @Test
    open fun updateEvent() = testRepoEvent(
        "update",
        EventUpdateRequest(
            event = CalendarEventStub.prepareResult { title = "event" }.toTransportUpdate(),
            debug = debug,
        ),
        prepareCtx(CalendarEventStub.prepareResult {
            title = "event"
            lock = CalendarEventLock(uuidNew)
        })
            .toTransportUpdate()
    )

    @Test
    open fun deleteEvent() = testRepoEvent(
        "delete",
        EventDeleteRequest(
            event = CalendarEventStub.get().toTransportDelete(),
            debug = debug,
        ),
        prepareCtx(CalendarEventStub.get())
            .toTransportDelete()
    )

    @Test
    open fun searchEvent() = testRepoEvent(
        "search",
        EventSearchRequest(
            eventFilter = EventSearchFilter("xx ev-0"),
            debug = debug,
        ),
        CalendarContext(
            state = CalendarState.RUNNING,
            eventsResponse = CalendarEventStub.prepareSearchList("xx")
                .onEach { it.permissionsClient.clear() }
                .sortedBy { it.id.asString() }
                .toMutableList()
        )
            .toTransportSearch()
    )

    private fun prepareCtx(event: CalendarEvent) = CalendarContext(
        state = CalendarState.RUNNING,
        eventResponse = event.apply {
            // Пока не реализована эта функциональность
            permissionsClient.clear()
        },
    )

    private inline fun <reified Req : Any, reified Res : IResponse> testRepoEvent(
        url: String,
        requestObj: Req,
        expectObj: Res,
    ) {
        webClient
            .post()
            .uri("/v2/event/$url")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                val sortedResp: IResponse = when (it) {
                    is EventSearchResponse -> it.copy(events = it.events?.sortedBy { it.id })
                    else -> it
                }
                assertThat(sortedResp).isEqualTo(expectObj)
            }
    }
}
