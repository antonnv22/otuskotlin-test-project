package ru.otus.otuskotlin.calendar.backend.repo.tests

import ru.otus.otuskotlin.calendar.common.models.CalendarEvent
import ru.otus.otuskotlin.calendar.common.models.CalendarUserId
import ru.otus.otuskotlin.calendar.common.repo.DbEventFilterRequest
import ru.otus.otuskotlin.calendar.common.repo.DbEventsResponseOk
import ru.otus.otuskotlin.calendar.common.repo.IRepoEvent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoEventSearchTest {
    abstract val repo: IRepoEvent

    protected open val initializedObjects: List<CalendarEvent> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchEvent(DbEventFilterRequest(ownerId = searchOwnerId))
        assertIs<DbEventsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[2]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object: BaseInitEvents("search") {

        val searchOwnerId = CalendarUserId("owner-124")
        override val initObjects: List<CalendarEvent> = listOf(
            createInitTestModel("ad1"),
            createInitTestModel("ad2", ownerId = searchOwnerId),
            createInitTestModel("ad3", ownerId = searchOwnerId),
        )
    }
}
