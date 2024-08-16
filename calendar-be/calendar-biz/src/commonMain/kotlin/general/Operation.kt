package ru.otus.otuskotlin.calendar.biz.general

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.CalendarCommand
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.cor.ICorChainDsl
import ru.otus.otuskotlin.calendar.cor.chain

fun ICorChainDsl<CalendarContext>.operation(
    title: String,
    command: CalendarCommand,
    block: ICorChainDsl<CalendarContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == CalendarState.RUNNING }
}
