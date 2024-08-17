package ru.otus.otuskotlin.calendar.backend.repo.tests

import ru.otus.otuskotlin.calendar.common.models.CalendarEvent
import ru.otus.otuskotlin.calendar.common.models.CalendarEventId
import ru.otus.otuskotlin.calendar.common.models.CalendarError
import ru.otus.otuskotlin.calendar.common.repo.DbEventIdRequest
import ru.otus.otuskotlin.calendar.common.repo.DbEventResponseErr
import ru.otus.otuskotlin.calendar.common.repo.DbEventResponseOk
import ru.otus.otuskotlin.calendar.common.repo.IRepoEvent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoEventReadTest {
    abstract val repo: IRepoEvent
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readEvent(DbEventIdRequest(readSucc.id))

        assertIs<DbEventResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        println("REQUESTING")
        val result = repo.readEvent(DbEventIdRequest(notFoundId))
        println("RESULT: $result")

        assertIs<DbEventResponseErr>(result)
        println("ERRORS: ${result.errors}")
        val error: CalendarError? = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitEvents("read") {
        override val initObjects: List<CalendarEvent> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = CalendarEventId("event-repo-read-notFound")

    }
}
