package ru.otus.otuskotlin.calendar.biz.repo

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.calendar.backend.repo.tests.EventRepositoryMock
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.repo.DbEventsResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {

    private val userId = CalendarUserId("321")
    private val command = CalendarCommand.SEARCH
    private val initEvent = CalendarEvent(
        id = CalendarEventId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        start = "2024-08-23T20:00:00Z",
        end = "2024-08-23T20:30:00Z",
        visibility = CalendarVisibility.VISIBLE_PUBLIC,
    )
    private val repo = EventRepositoryMock(
        invokeSearchEvent = {
            DbEventsResponseOk(
                data = listOf(initEvent),
            )
        }
    )
    private val settings = CalendarCorSettings(repoTest = repo)
    private val processor = CalendarEventProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = CalendarContext(
            command = command,
            state = CalendarState.NONE,
            workMode = CalendarWorkMode.TEST,
            eventFilterRequest = CalendarEventFilter(
                searchString = "abc"
            ),
        )
        processor.exec(ctx)
        assertEquals(CalendarState.FINISHING, ctx.state)
        assertEquals(1, ctx.eventsResponse.size)
    }
}
