package repo

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.calendar.backend.repo.tests.EventRepositoryMock
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.repo.DbEventResponseOk
import ru.otus.otuskotlin.calendar.common.repo.errorNotFound
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initEvent = CalendarEvent(
    id = CalendarEventId("123"),
    title = "abc",
    description = "abc",
    start = "2024-08-23T20:00:00Z",
    end = "2024-08-23T20:30:00Z",
    visibility = CalendarVisibility.VISIBLE_PUBLIC,
)
private val repo = EventRepositoryMock(
        invokeReadEvent = {
            if (it.id == initEvent.id) {
                DbEventResponseOk(
                    data = initEvent,
                )
            } else errorNotFound(it.id)
        }
    )
private val settings = CalendarCorSettings(repoTest = repo)
private val processor = CalendarEventProcessor(settings)

fun repoNotFoundTest(command: CalendarCommand) = runTest {
    val ctx = CalendarContext(
        command = command,
        state = CalendarState.NONE,
        workMode = CalendarWorkMode.TEST,
        eventRequest = CalendarEvent(
            id = CalendarEventId("12345"),
            title = "xyz",
            description = "xyz",
            start = "2024-08-23T20:00:00Z",
            end = "2024-08-23T20:30:00Z",
            visibility = CalendarVisibility.VISIBLE_TO_GROUP,
            lock = CalendarEventLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(CalendarState.FAILING, ctx.state)
    assertEquals(CalendarEvent(), ctx.eventResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
