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
        eventRequest = CalendarEvent(
            id = stub.id,
            title = "Технический созвон",
            description = "Технический созвон",
            start = "2024-08-23T19:00:00Z",
            end = "2024-08-23T19:30:00Z",
            visibility = CalendarVisibility.VISIBLE_PUBLIC,
            lock = CalendarEventLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CalendarState.FAILING, ctx.state)
    assertEquals("Технический созвон", ctx.eventValidated.title)
}

fun validationTitleTrim(command: CalendarCommand, processor: CalendarEventProcessor) = runTest {
    val ctx = CalendarContext(
        command = command,
        state = CalendarState.NONE,
        workMode = CalendarWorkMode.TEST,
        eventRequest = CalendarEvent(
            id = stub.id,
            title = " \n\t Технический созвон \t\n ",
            description = "Технический созвон",
            start = "2024-08-23T19:00:00Z",
            end = "2024-08-23T19:30:00Z",
            visibility = CalendarVisibility.VISIBLE_PUBLIC,
            lock = CalendarEventLock("123-234-abc-ABC"),
        ),
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
        eventRequest = CalendarEvent(
            id = stub.id,
            title = "",
            description = "Технический созвон",
            start = "2024-08-23T19:00:00Z",
            end = "2024-08-23T19:30:00Z",
            visibility = CalendarVisibility.VISIBLE_PUBLIC,
            lock = CalendarEventLock("123-234-abc-ABC"),
        ),
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
        eventRequest = CalendarEvent(
            id = CalendarEventId("123"),
            title = "!@#$%^&*(),.{}",
            description = "Технический созвон",
            start = "2024-08-23T19:00:00Z",
            end = "2024-08-23T19:30:00Z",
            visibility = CalendarVisibility.VISIBLE_PUBLIC,
            lock = CalendarEventLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CalendarState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}
