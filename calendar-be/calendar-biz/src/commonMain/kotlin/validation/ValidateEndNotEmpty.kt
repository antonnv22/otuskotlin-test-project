package ru.otus.otuskotlin.calendar.biz.validation

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.helpers.errorValidation
import ru.otus.otuskotlin.calendar.common.helpers.fail
import ru.otus.otuskotlin.calendar.cor.ICorChainDsl
import ru.otus.otuskotlin.calendar.cor.worker

fun ICorChainDsl<CalendarContext>.validateEndNotEmpty(title: String) = worker {
    this.title = title
    on { eventValidating.start.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "end",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
