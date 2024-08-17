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

class BizRepoUpdateTest {

    private val userId = CalendarUserId("321")
    private val command = CalendarCommand.UPDATE
    private val initEvent = CalendarEvent(
        id = CalendarEventId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        start = "2024-08-23T20:00:00Z",
        end = "2024-08-23T20:30:00Z",
        visibility = CalendarVisibility.VISIBLE_PUBLIC,
        lock = CalendarEventLock("123-234-abc-ABC"),
    )
    private val repo = EventRepositoryMock(
        invokeReadEvent = {
            DbEventResponseOk(
                data = initEvent,
            )
        },
        invokeUpdateEvent = {
            DbEventResponseOk(
                data = CalendarEvent(
                    id = CalendarEventId("123"),
                    title = "xyz",
                    description = "xyz",
                    start = "2024-08-23T20:00:00Z",
                    end = "2024-08-23T20:30:00Z",
                    visibility = CalendarVisibility.VISIBLE_TO_GROUP,
                    lock = CalendarEventLock("123-234-abc-ABC"),
                )
            )
        }
    )
    private val settings = CalendarCorSettings(repoTest = repo)
    private val processor = CalendarEventProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val eventToUpdate = CalendarEvent(
            id = CalendarEventId("123"),
            title = "xyz",
            description = "xyz",
            start = "2024-08-23T20:00:00Z",
            end = "2024-08-23T20:30:00Z",
            visibility = CalendarVisibility.VISIBLE_TO_GROUP,
            lock = CalendarEventLock("123-234-abc-ABC"),
        )
        val ctx = CalendarContext(
            command = command,
            state = CalendarState.NONE,
            workMode = CalendarWorkMode.TEST,
            eventRequest = eventToUpdate,
        )
        processor.exec(ctx)
        assertEquals(CalendarState.FINISHING, ctx.state)
        assertEquals(eventToUpdate.id, ctx.eventResponse.id)
        assertEquals(eventToUpdate.title, ctx.eventResponse.title)
        assertEquals(eventToUpdate.description, ctx.eventResponse.description)
        assertEquals(eventToUpdate.visibility, ctx.eventResponse.visibility)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
