package ru.otus.otuskotlin.calendar.e2e.be.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import ru.otus.otuskotlin.calendar.api.v1.models.*
import ru.otus.otuskotlin.calendar.blackbox.test.action.beValidId
import ru.otus.otuskotlin.calendar.blackbox.test.action.beValidLock
import ru.otus.otuskotlin.calendar.e2e.be.fixture.client.Client

suspend fun Client.updateEvent(event: EventUpdateObject, debug: EventDebug = debugStubV1): EventResponseObject =
    updateEvent(event, debug = debug) {
        it should haveSuccessResult
        it.event shouldNotBe null
        it.event?.apply {
            if (event.title != null)
                title shouldBe event.title
            if (event.description != null)
                description shouldBe event.description
            if (event.visibility != null)
                visibility shouldBe event.visibility
        }
        it.event!!
    }

suspend fun <T> Client.updateEvent(event: EventUpdateObject, debug: EventDebug = debugStubV1, block: (EventUpdateResponse) -> T): T {
    val id = event.id
    val lock = event.lock
    return withClue("updatedV1: $id, lock: $lock, set: $event") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "event/update", EventUpdateRequest(
                debug = debug,
                event = event.copy(id = id, lock = lock)
            )
        ) as EventUpdateResponse

        response.asClue(block)
    }
}
