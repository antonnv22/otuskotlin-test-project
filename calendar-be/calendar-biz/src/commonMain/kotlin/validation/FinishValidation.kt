package ru.otus.otuskotlin.calendar.biz.validation

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.cor.ICorChainDsl
import ru.otus.otuskotlin.calendar.cor.worker

fun ICorChainDsl<CalendarContext>.finishEventValidation(title: String) = worker {
    this.title = title
    on { state == CalendarState.RUNNING }
    handle {
        eventValidated = eventValidating
    }
}

fun ICorChainDsl<CalendarContext>.finishEventFilterValidation(title: String) = worker {
    this.title = title
    on { state == CalendarState.RUNNING }
    handle {
        eventFilterValidated = eventFilterValidating
    }
}
