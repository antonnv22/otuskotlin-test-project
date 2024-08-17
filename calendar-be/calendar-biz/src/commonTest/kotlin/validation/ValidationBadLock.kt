package validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.stubs.CalendarEventStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationLockCorrect(command: CalendarCommand, processor: CalendarEventProcessor) = runTest {
    val ctx = CalendarContext(
        command = command,
        state = CalendarState.NONE,
        workMode = CalendarWorkMode.TEST,
        eventRequest = CalendarEventStub.get(),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CalendarState.FAILING, ctx.state)
}

fun validationLockTrim(command: CalendarCommand, processor: CalendarEventProcessor) = runTest {
    val ctx = CalendarContext(
        command = command,
        state = CalendarState.NONE,
        workMode = CalendarWorkMode.TEST,
        eventRequest = CalendarEventStub.prepareResult {
            lock = CalendarEventLock(" \n\t 123-234-abc-ABC \n\t ")
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CalendarState.FAILING, ctx.state)
}

fun validationLockEmpty(command: CalendarCommand, processor: CalendarEventProcessor) = runTest {
    val ctx = CalendarContext(
        command = command,
        state = CalendarState.NONE,
        workMode = CalendarWorkMode.TEST,
        eventRequest = CalendarEventStub.prepareResult {
            lock = CalendarEventLock("")
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CalendarState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationLockFormat(command: CalendarCommand, processor: CalendarEventProcessor) = runTest {
    val ctx = CalendarContext(
        command = command,
        state = CalendarState.NONE,
        workMode = CalendarWorkMode.TEST,
        eventRequest = CalendarEventStub.prepareResult {
            lock = CalendarEventLock("!@#\$%^&*(),.{}")
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CalendarState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
