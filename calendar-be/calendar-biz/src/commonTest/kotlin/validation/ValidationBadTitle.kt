package ru.otus.otuskotlin.calendar.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.stubs.CalendarEventStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = CalendarEventStub.get()

fun validationTitleCorrect(command: CalendarCommand, processor: CalendarEventProcessor) = runTest {
    val ctx = CalendarContext(
        command = command,
        state = CalendarState.NONE,
        workMode = CalendarWorkMode.TEST,
        eventRequest = CalendarEventStub.get(),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CalendarState.FAILING, ctx.state)
    assertEquals("Дейли", ctx.eventValidated.title)
}

fun validationTitleTrim(command: CalendarCommand, processor: CalendarEventProcessor) = runTest {
    val ctx = CalendarContext(
        command = command,
        state = CalendarState.NONE,
        workMode = CalendarWorkMode.TEST,
        eventRequest = CalendarEventStub.prepareResult {
            title = " \n\t Технический созвон \n\t"
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CalendarState.FAILING, ctx.state)
    assertEquals("Технический созвон", ctx.eventValidated.title)
}

fun validationTitleEmpty(command: CalendarCommand, processor: CalendarEventProcessor) = runTest {
    val ctx = CalendarContext(
        command = command,
        state = CalendarState.NONE,
        workMode = CalendarWorkMode.TEST,
        eventRequest = CalendarEventStub.prepareResult {
            title = ""
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CalendarState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

fun validationTitleSymbols(command: CalendarCommand, processor: CalendarEventProcessor) = runTest {
    val ctx = CalendarContext(
        command = command,
        state = CalendarState.NONE,
        workMode = CalendarWorkMode.TEST,
        eventRequest = CalendarEventStub.prepareResult {
            title = "!@#$%^&*(),.{}"
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CalendarState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}
