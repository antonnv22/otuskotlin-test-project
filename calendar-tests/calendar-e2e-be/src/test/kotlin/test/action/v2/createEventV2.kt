package ru.otus.otuskotlin.calendar.e2e.be.test.action.v2

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch
import ru.otus.otuskotlin.calendar.api.v2.models.*
import ru.otus.otuskotlin.calendar.e2e.be.fixture.client.Client

suspend fun Client.createEvent(event: EventCreateObject = someCreateEvent, debug: EventDebug = debugStubV2): EventResponseObject = createEvent(event, debug = debug) {
    it should haveSuccessResult
    it.event shouldNotBe null
    it.event?.apply {
        title shouldBe event.title
        description shouldBe event.description
        start shouldBe event.start
        end shouldBe event.end
        visibility shouldBe event.visibility
        id.toString() shouldMatch Regex("^[\\d\\w_-]+\$")
        lock.toString() shouldMatch Regex("^[\\d\\w_-]+\$")
    }
    it.event!!
}

suspend fun <T> Client.createEvent(event: EventCreateObject = someCreateEvent, debug: EventDebug = debugStubV2, block: (EventCreateResponse) -> T): T =
    withClue("createEventV2: $event") {
        val response: EventCreateResponse = sendAndReceive(
            "event/create", EventCreateRequest(
                debug = debug,
                event = event
            )
        )

        response.asClue(block)
    }
