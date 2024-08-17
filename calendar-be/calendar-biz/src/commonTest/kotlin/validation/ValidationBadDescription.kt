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

fun validationDescriptionCorrect(command: CalendarCommand, processor: CalendarEventProcessor) = runTest {
    val ctx = CalendarContext(
        command = command,
        state = CalendarState.NONE,
        workMode = CalendarWorkMode.TEST,
        eventRequest = CalendarEventStub.get(),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CalendarState.FAILING, ctx.state)
    assertEquals("Ежедневное дейли", ctx.eventValidated.description)
}

fun validationDescriptionTrim(command: CalendarCommand, processor: CalendarEventProcessor) = runTest {
    val ctx = CalendarContext(
        command = command,
        state = CalendarState.NONE,
        workMode = CalendarWorkMode.TEST,
        eventRequest = CalendarEventStub.prepareResult {
            description = " \n\t Технический созвон \n\t"
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CalendarState.FAILING, ctx.state)
    assertEquals("Технический созвон", ctx.eventValidated.description)
}

fun validationDescriptionEmpty(command: CalendarCommand, processor: CalendarEventProcessor) = runTest {
    val ctx = CalendarContext(
        command = command,
        state = CalendarState.NONE,
        workMode = CalendarWorkMode.TEST,
        eventRequest = CalendarEventStub.prepareResult {
            description = ""
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CalendarState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}

fun validationDescriptionSymbols(command: CalendarCommand, processor: CalendarEventProcessor) = runTest {
    val ctx = CalendarContext(
        command = command,
        state = CalendarState.NONE,
        workMode = CalendarWorkMode.TEST,
        eventRequest = CalendarEventStub.prepareResult {
            description = "!@#$%^&*(),.{}"
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CalendarState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}
