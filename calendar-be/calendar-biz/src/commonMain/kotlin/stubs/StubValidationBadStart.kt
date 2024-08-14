package ru.otus.otuskotlin.calendar.biz.stubs

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.helpers.fail
import ru.otus.otuskotlin.calendar.common.models.CalendarError
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.common.stubs.CalendarStubs
import ru.otus.otuskotlin.calendar.cor.ICorChainDsl
import ru.otus.otuskotlin.calendar.cor.worker

fun ICorChainDsl<CalendarContext>.stubValidationBadStart(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для начала события
    """.trimIndent()

    on { stubCase == CalendarStubs.BAD_START && state == CalendarState.RUNNING }
    handle {
        fail(
            CalendarError(
                group = "validation",
                code = "validation-start",
                field = "start",
                message = "Wrong start field"
            )
        )
    }
}
