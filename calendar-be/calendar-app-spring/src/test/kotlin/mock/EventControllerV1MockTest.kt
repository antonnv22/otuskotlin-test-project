package ru.otus.otuskotlin.calendar.app.spring.mock

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.wheneverBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.calendar.api.v1.models.*
import ru.otus.otuskotlin.calendar.app.spring.config.EventConfig
import ru.otus.otuskotlin.calendar.app.spring.controllers.EventControllerV1Fine
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.mappers.v1.*
import ru.otus.otuskotlin.calendar.stubs.CalendarEventStub
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(EventControllerV1Fine::class, EventConfig::class)
internal class EventControllerV1MockTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: CalendarEventProcessor

    @BeforeEach
    fun tearUp() {
        wheneverBlocking { processor.exec(any()) }.then {
            it.getArgument<CalendarContext>(0).apply {
                eventResponse = CalendarEventStub.get()
                eventsResponse = CalendarEventStub.prepareSearchList("daily").toMutableList()
            }
        }
    }

    @Test
    fun createEvent() = testStubEvent(
        "/v1/event/create",
        EventCreateRequest(),
        CalendarContext(eventResponse = CalendarEventStub.get()).toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readEvent() = testStubEvent(
        "/v1/event/read",
        EventReadRequest(),
        CalendarContext(eventResponse = CalendarEventStub.get()).toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updateEvent() = testStubEvent(
        "/v1/event/update",
        EventUpdateRequest(),
        CalendarContext(eventResponse = CalendarEventStub.get()).toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deleteEvent() = testStubEvent(
        "/v1/event/delete",
        EventDeleteRequest(),
        CalendarContext(eventResponse = CalendarEventStub.get()).toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchEvent() = testStubEvent(
        "/v1/event/search",
        EventSearchRequest(),
        CalendarContext(eventsResponse = CalendarEventStub.prepareSearchList("daily").toMutableList())
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
