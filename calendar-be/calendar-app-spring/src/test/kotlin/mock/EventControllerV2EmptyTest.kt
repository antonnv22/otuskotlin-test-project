package ru.otus.otuskotlin.calendar.app.spring.mock

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.calendar.api.v2.mappers.*
import ru.otus.otuskotlin.calendar.api.v2.models.*
import ru.otus.otuskotlin.calendar.app.spring.config.EventConfig
import ru.otus.otuskotlin.calendar.app.spring.controllers.EventControllerV2Fine
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarContext
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(EventControllerV2Fine::class, EventConfig::class)
internal class EventControllerV2EmptyTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: CalendarEventProcessor

    @Test
    fun createEvent() = testStubEvent(
        "/v2/event/create",
        EventCreateRequest(),
        CalendarContext().toTransportCreate()
    )

    @Test
    fun readEvent() = testStubEvent(
        "/v2/event/read",
        EventReadRequest(),
        CalendarContext().toTransportRead()
    )

    @Test
    fun updateEvent() = testStubEvent(
        "/v2/event/update",
        EventUpdateRequest(),
        CalendarContext().toTransportUpdate()
    )

    @Test
    fun deleteEvent() = testStubEvent(
        "/v2/event/delete",
        EventDeleteRequest(),
        CalendarContext().toTransportDelete()
    )

    @Test
    fun searchEvent() = testStubEvent(
        "/v2/event/search",
        EventSearchRequest(),
        CalendarContext().toTransportSearch()
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
