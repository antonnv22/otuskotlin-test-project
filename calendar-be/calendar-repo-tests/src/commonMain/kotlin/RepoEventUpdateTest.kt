package ru.otus.otuskotlin.calendar.backend.repo.tests

import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoEventUpdateTest {
    abstract val repo: IRepoEvent
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = CalendarEventId("ad-repo-update-not-found")
    protected val lockBad = CalendarEventLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = CalendarEventLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        CalendarEvent(
            id = updateSucc.id,
            title = "update object",
            description = "update object description",
            ownerId = CalendarUserId("owner-123"),
            visibility = CalendarVisibility.VISIBLE_TO_GROUP,
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = CalendarEvent(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = CalendarUserId("owner-123"),
        visibility = CalendarVisibility.VISIBLE_TO_GROUP,
        lock = initObjects.first().lock,
    )
    private val reqUpdateConc by lazy {
        CalendarEvent(
            id = updateConc.id,
            title = "update object not found",
            description = "update object not found description",
            ownerId = CalendarUserId("owner-123"),
            visibility = CalendarVisibility.VISIBLE_TO_GROUP,
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateEvent(DbEventRequest(reqUpdateSucc))
        println("ERRORS: ${(result as? DbEventResponseErr)?.errors}")
        println("ERRORSWD: ${(result as? DbEventResponseErrWithData)?.errors}")
        assertIs<DbEventResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.title, result.data.title)
        assertEquals(reqUpdateSucc.description, result.data.description)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateEvent(DbEventRequest(reqUpdateNotFound))
        assertIs<DbEventResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateEvent(DbEventRequest(reqUpdateConc))
        assertIs<DbEventResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitEvents("update") {
        override val initObjects: List<CalendarEvent> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
