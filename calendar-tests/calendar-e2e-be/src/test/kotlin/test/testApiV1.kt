package ru.otus.otuskotlin.calendar.e2e.be.test

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.collections.shouldExistInOrder
import io.kotest.matchers.shouldBe
import ru.otus.otuskotlin.calendar.api.v1.models.EventDebug
import ru.otus.otuskotlin.calendar.api.v1.models.EventSearchFilter
import ru.otus.otuskotlin.calendar.api.v1.models.EventUpdateObject
import ru.otus.otuskotlin.calendar.e2e.be.fixture.client.Client
import ru.otus.otuskotlin.calendar.e2e.be.test.action.v1.*

fun FunSpec.testApiV1(client: Client, prefix: String = "", debug: EventDebug = debugStubV1) {
    context("${prefix}v1") {
        test("Create Event ok") {
            client.createEvent(debug = debug)
        }

        test("Read Event ok") {
            val created = client.createEvent(debug = debug)
            client.readEvent(created.id, debug = debug).asClue {
                it shouldBe created
            }
        }

        test("Update Event ok") {
            val created = client.createEvent(debug = debug)
            val updateEvent = EventUpdateObject(
                id = created.id,
                lock = created.lock,
                title = "Retro",
                description = created.description,
                start = "2024-08-23T20:00:00Z",
                end = "2024-08-23T20:30:00Z",
                visibility = created.visibility,
            )
            client.updateEvent(updateEvent, debug = debug)
        }

        test("Delete Event ok") {
            val created = client.createEvent(debug = debug)
            client.deleteEvent(created, debug = debug)
//            client.readEvent(created.id) {
//                 it should haveError("not-found")
//            }
        }

        test("Search Event ok") {
            val created1 = client.createEvent(someCreateEvent.copy(title = "Retro Sprint 1"), debug = debug)
            val created2 = client.createEvent(someCreateEvent.copy(title = "Retro Sprint 2"), debug = debug)

            withClue("Search Retro") {
                val results = client.searchEvent(search = EventSearchFilter(searchString = "Retro"), debug = debug)
                results shouldExist { it.title == created1.title }
                results shouldExist { it.title == created2.title }
            }

            withClue("Search Sprint 1") {
                client.searchEvent(search = EventSearchFilter(searchString = "Sprint 1"), debug = debug)
                    .shouldExistInOrder({ it.title == created1.title })
            }
        }
    }
}
