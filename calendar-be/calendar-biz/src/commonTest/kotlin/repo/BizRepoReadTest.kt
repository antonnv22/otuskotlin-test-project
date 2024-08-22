package ru.otus.otuskotlin.calendar.biz.repo

import kotlinx.coroutines.test.runTest
import repo.repoNotFoundTest
import ru.otus.otuskotlin.calendar.backend.repo.tests.EventRepositoryMock
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.repo.DbEventResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoReadTest {

    private val userId = CalendarUserId("321")
    private val command = CalendarCommand.READ
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
        invokeReadEvent = {
            DbEventResponseOk(
                data = initEvent,
            )
        }
    )
    private val settings = CalendarCorSettings(repoTest = repo)
    private val processor = CalendarEventProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = CalendarContext(
            command = command,
            state = CalendarState.NONE,
            workMode = CalendarWorkMode.TEST,
            eventRequest = CalendarEvent(
                id = CalendarEventId("123"),
            ),
        )
        processor.exec(ctx)
        assertEquals(CalendarState.FINISHING, ctx.state)
        assertEquals(initEvent.id, ctx.eventResponse.id)
        assertEquals(initEvent.title, ctx.eventResponse.title)
        assertEquals(initEvent.description, ctx.eventResponse.description)
        assertEquals(initEvent.visibility, ctx.eventResponse.visibility)
    }

    @Test
    fun repoReeventNotFoundTest() = repoNotFoundTest(command)
}
