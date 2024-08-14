package ru.otus.otuskotlin.calendar.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.CalendarEvent
import ru.otus.otuskotlin.calendar.common.models.CalendarEventFilter
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateTitleHasContentTest {
    @Test
    fun emptyString() = runTest {
        val ctx = CalendarContext(state = CalendarState.RUNNING, eventValidating = CalendarEvent(title = ""))
        chain.exec(ctx)
        assertEquals(CalendarState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = CalendarContext(state = CalendarState.RUNNING, eventValidating = CalendarEvent(title = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(CalendarState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-title-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = CalendarContext(state = CalendarState.RUNNING, eventFilterValidating = CalendarEventFilter(searchString = "Ж"))
        chain.exec(ctx)
        assertEquals(CalendarState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateTitleHasContent("")
        }.build()
    }
}
