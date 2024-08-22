package ru.otus.otuskotlin.calendar.backend.repo.tests

import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.repo.DbEventRequest
import ru.otus.otuskotlin.calendar.common.repo.DbEventResponseOk
import ru.otus.otuskotlin.calendar.repo.common.IRepoEventInitializable
import kotlin.test.*


abstract class RepoEventCreateTest {
    abstract val repo: IRepoEventInitializable
    protected open val uuidNew = CalendarEventId("10000000-0000-0000-0000-000000000001")

    private val createObj = CalendarEvent(
        title = "create object",
        description = "create object description",
        ownerId = CalendarUserId("owner-123"),
        visibility = CalendarVisibility.VISIBLE_TO_GROUP,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createEvent(DbEventRequest(createObj))
        val expected = createObj
        assertIs<DbEventResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(expected.title, result.data.title)
        assertEquals(expected.description, result.data.description)
        assertNotEquals(CalendarEventId.NONE, result.data.id)
    }

    companion object : BaseInitEvents("create") {
        override val initObjects: List<CalendarEvent> = emptyList()
    }
}
