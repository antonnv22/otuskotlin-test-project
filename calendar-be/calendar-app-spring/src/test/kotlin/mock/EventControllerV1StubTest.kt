package ru.otus.otuskotlin.calendar.app.spring.mock

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.calendar.app.spring.config.EventConfig
import ru.otus.otuskotlin.calendar.app.spring.controllers.EventControllerV1Fine
import ru.otus.otuskotlin.calendar.api.v1.models.*
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.mappers.v1.*
import ru.otus.otuskotlin.calendar.stubs.CalendarEventStub
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(EventControllerV1Fine::class, EventConfig::class)
internal class EventControllerV1StubTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun createEvent() = testStubEvent(
        "/v1/event/create",
        EventCreateRequest(),
        CalendarContext(eventResponse = CalendarEventStub.get(), state = CalendarState.FINISHING)
            .toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readEvent() = testStubEvent(
        "/v1/event/read",
        EventReadRequest(),
        CalendarContext(eventResponse = CalendarEventStub.get(), state = CalendarState.FINISHING)
            .toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updateEvent() = testStubEvent(
        "/v1/event/update",
        EventUpdateRequest(),
        CalendarContext(eventResponse = CalendarEventStub.get(), state = CalendarState.FINISHING)
            .toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deleteEvent() = testStubEvent(
        "/v1/event/delete",
        EventDeleteRequest(),
        CalendarContext(eventResponse = CalendarEventStub.get(), state = CalendarState.FINISHING)
            .toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchEvent() = testStubEvent(
        "/v1/event/search",
        EventSearchRequest(),
        CalendarContext(
            eventsResponse = CalendarEventStub.prepareSearchList("event search").toMutableList(),
            state = CalendarState.FINISHING
        )
            .toTransportSearch().copy(responseType = "search")
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubEvent(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                assertThat(it).isEqualTo(responseObj)
            }
    }
}
