package ru.otus.otuskotlin.calendar.e2e.be.test.action.v2

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import ru.otus.otuskotlin.calendar.api.v2.models.*
import ru.otus.otuskotlin.calendar.e2e.be.fixture.client.Client

suspend fun Client.searchEvent(search: EventSearchFilter, debug: EventDebug = debugStubV2): List<EventResponseObject> = searchEvent(search, debug = debug) {
    it should haveSuccessResult
    it.events ?: listOf()
}

suspend fun <T> Client.searchEvent(search: EventSearchFilter, debug: EventDebug = debugStubV2, block: (EventSearchResponse) -> T): T =
    withClue("searchEventV2: $search") {
        val response: EventSearchResponse = sendAndReceive(
            "event/search",
            EventSearchRequest(
                debug = debug,
                eventFilter = search,
            )
        )

        response.asClue(block)
    }
