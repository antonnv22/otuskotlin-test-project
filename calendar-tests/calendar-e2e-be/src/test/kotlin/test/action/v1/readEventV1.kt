package ru.otus.otuskotlin.calendar.e2e.be.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldNotBe
import ru.otus.otuskotlin.calendar.api.v1.models.*
import ru.otus.otuskotlin.calendar.blackbox.test.action.beValidId
import ru.otus.otuskotlin.calendar.e2e.be.fixture.client.Client

suspend fun Client.readEvent(id: String?, debug: EventDebug = debugStubV1): EventResponseObject = readEvent(id, debug = debug) {
    it should haveSuccessResult
    it.event shouldNotBe null
    it.event!!
}

suspend fun <T> Client.readEvent(id: String?, debug: EventDebug = debugStubV1, block: (EventReadResponse) -> T): T =
    withClue("readEventV1: $id") {
        id should beValidId

        val response = sendAndReceive(
            "event/read",
            EventReadRequest(
                requestType = "read",
                debug = debug,
                event = EventReadObject(id = id)
            )
        ) as EventReadResponse

        response.asClue(block)
    }
