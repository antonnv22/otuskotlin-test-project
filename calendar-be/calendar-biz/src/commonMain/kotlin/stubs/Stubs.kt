package ru.otus.otuskotlin.calendar.biz.stubs

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.common.models.CalendarWorkMode
import ru.otus.otuskotlin.calendar.cor.ICorChainDsl
import ru.otus.otuskotlin.calendar.cor.chain

fun ICorChainDsl<CalendarContext>.stubs(title: String, block: ICorChainDsl<CalendarContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == CalendarWorkMode.STUB && state == CalendarState.RUNNING }
}
