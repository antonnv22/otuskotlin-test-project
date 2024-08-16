package ru.otus.otuskotlin.calendar.biz.validation

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.cor.ICorChainDsl
import ru.otus.otuskotlin.calendar.cor.chain

fun ICorChainDsl<CalendarContext>.validation(block: ICorChainDsl<CalendarContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == CalendarState.RUNNING }
}
