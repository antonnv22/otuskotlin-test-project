package ru.otus.otuskotlin.calendar.biz.repo

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.calendar.backend.repo.tests.EventRepositoryMock
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.repo.DbEventResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val userId = CalendarUserId("321")
    private val command = CalendarCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = EventRepositoryMock(
        invokeCreateEvent = {
            DbEventResponseOk(
                data = CalendarEvent(
                    id = CalendarEventId(uuid),
                    title = it.event.title,
                    description = it.event.description,
                    start = it.event.start,
                    end = it.event.end,
                    ownerId = userId,
                    visibility = it.event.visibility,
                )
            )
        }
    )
    private val settings = CalendarCorSettings(
        repoTest = repo
    )
    private val processor = CalendarEventProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = CalendarContext(
            command = command,
            state = CalendarState.NONE,
            workMode = CalendarWorkMode.TEST,
            eventRequest = CalendarEvent(
                title = "abc",
                description = "abc",
                start = "2024-08-23T20:00:00Z",
                end = "2024-08-23T20:30:00Z",
                visibility = CalendarVisibility.VISIBLE_PUBLIC,
            ),
        )
        processor.exec(ctx)
        assertEquals(CalendarState.FINISHING, ctx.state)
        assertNotEquals(CalendarEventId.NONE, ctx.eventResponse.id)
        assertEquals("abc", ctx.eventResponse.title)
        assertEquals("abc", ctx.eventResponse.description)
        assertEquals(CalendarVisibility.VISIBLE_PUBLIC, ctx.eventResponse.visibility)
    }
}
