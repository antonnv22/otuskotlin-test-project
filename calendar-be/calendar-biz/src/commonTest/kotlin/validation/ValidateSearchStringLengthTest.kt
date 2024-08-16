package ru.otus.otuskotlin.calendar.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.CalendarEventFilter
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runTest {
        val ctx = CalendarContext(state = CalendarState.RUNNING, eventFilterValidating = CalendarEventFilter(searchString = ""))
        chain.exec(ctx)
        assertEquals(CalendarState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = CalendarContext(state = CalendarState.RUNNING, eventFilterValidating = CalendarEventFilter(searchString = "  "))
        chain.exec(ctx)
        assertEquals(CalendarState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = CalendarContext(state = CalendarState.RUNNING, eventFilterValidating = CalendarEventFilter(searchString = "12"))
        chain.exec(ctx)
        assertEquals(CalendarState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = CalendarContext(state = CalendarState.RUNNING, eventFilterValidating = CalendarEventFilter(searchString = "123"))
        chain.exec(ctx)
        assertEquals(CalendarState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = CalendarContext(state = CalendarState.RUNNING, eventFilterValidating = CalendarEventFilter(searchString = "12".repeat(51)))
        chain.exec(ctx)
        assertEquals(CalendarState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}
