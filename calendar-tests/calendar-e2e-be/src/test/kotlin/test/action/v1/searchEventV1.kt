package ru.otus.otuskotlin.calendar.e2e.be.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import ru.otus.otuskotlin.calendar.api.v1.models.*
import ru.otus.otuskotlin.calendar.e2e.be.fixture.client.Client

suspend fun Client.searchEvent(search: EventSearchFilter, debug: EventDebug = debugStubV1): List<EventResponseObject> = searchEvent(search, debug = debug) {
    it should haveSuccessResult
    it.events ?: listOf()
}

suspend fun <T> Client.searchEvent(search: EventSearchFilter, debug: EventDebug = debugStubV1, block: (EventSearchResponse) -> T): T =
    withClue("searchEventV1: $search") {
        val response = sendAndReceive(
            "event/search",
            EventSearchRequest(
                requestType = "search",
                debug = debug,
                eventFilter = search,
            )
        ) as EventSearchResponse

        response.asClue(block)
    }
