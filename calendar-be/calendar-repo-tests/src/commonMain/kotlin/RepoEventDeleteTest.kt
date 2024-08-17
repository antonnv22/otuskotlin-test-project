package ru.otus.otuskotlin.calendar.backend.repo.tests

import ru.otus.otuskotlin.calendar.common.models.CalendarEvent
import ru.otus.otuskotlin.calendar.common.models.CalendarEventId
import ru.otus.otuskotlin.calendar.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoEventDeleteTest {
    abstract val repo: IRepoEvent
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = CalendarEventId("event-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteEvent(DbEventIdRequest(deleteSucc.id, lock = lockOld))
        assertIs<DbEventResponseOk>(result)
        assertEquals(deleteSucc.title, result.data.title)
        assertEquals(deleteSucc.description, result.data.description)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readEvent(DbEventIdRequest(notFoundId, lock = lockOld))

        assertIs<DbEventResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deleteEvent(DbEventIdRequest(deleteConc.id, lock = lockBad))

        assertIs<DbEventResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitEvents("delete") {
        override val initObjects: List<CalendarEvent> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
