package ru.otus.otuskotlin.calendar.biz.repo

import kotlinx.coroutines.test.runTest
import repo.repoNotFoundTest
import ru.otus.otuskotlin.calendar.backend.repo.tests.EventRepositoryMock
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.repo.DbEventResponseErr
import ru.otus.otuskotlin.calendar.common.repo.DbEventResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BizRepoDeleteTest {

    private val userId = CalendarUserId("321")
    private val command = CalendarCommand.DELETE
    private val initEvent = CalendarEvent(
        id = CalendarEventId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        visibility = CalendarVisibility.VISIBLE_PUBLIC,
        lock = CalendarEventLock("123-234-abc-ABC"),
    )
    private val repo = EventRepositoryMock(
        invokeReadEvent = {
            DbEventResponseOk(
                data = initEvent,
            )
        },
        invokeDeleteEvent = {
            if (it.id == initEvent.id)
                DbEventResponseOk(
                    data = initEvent
                )
            else DbEventResponseErr()
        }
    )
    private val settings by lazy {
        CalendarCorSettings(
            repoTest = repo
        )
    }
    private val processor = CalendarEventProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val eventToUpdate = CalendarEvent(
            id = CalendarEventId("123"),
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
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initEvent.id, ctx.eventResponse.id)
        assertEquals(initEvent.title, ctx.eventResponse.title)
        assertEquals(initEvent.description, ctx.eventResponse.description)
        assertEquals(initEvent.visibility, ctx.eventResponse.visibility)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
