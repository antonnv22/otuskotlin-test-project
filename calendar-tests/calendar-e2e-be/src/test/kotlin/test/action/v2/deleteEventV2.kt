package ru.otus.otuskotlin.calendar.e2e.be.test.action.v2

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import ru.otus.otuskotlin.calendar.api.v2.models.*
import ru.otus.otuskotlin.calendar.blackbox.test.action.beValidId
import ru.otus.otuskotlin.calendar.blackbox.test.action.beValidLock
import ru.otus.otuskotlin.calendar.e2e.be.fixture.client.Client

suspend fun Client.deleteEvent(event: EventResponseObject, debug: EventDebug = debugStubV2) {
    val id = event.id
    val lock = event.lock
    withClue("deleteEventV2: $id, lock: $lock") {
        id should beValidId
        lock should beValidLock

        val response: EventDeleteResponse = sendAndReceive(
            "event/delete",
            EventDeleteRequest(
                debug = debug,
                event = EventDeleteObject(id = id, lock = lock)
            )
        )

        response.asClue {
            response should haveSuccessResult
            response.event shouldBe event
//            response.event?.id shouldBe id
        }
    }
}
