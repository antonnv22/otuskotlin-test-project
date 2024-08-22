package ru.otus.otuskotlin.calendar.biz.repo

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.common.models.CalendarWorkMode
import ru.otus.otuskotlin.calendar.cor.ICorChainDsl
import ru.otus.otuskotlin.calendar.cor.worker

fun ICorChainDsl<CalendarContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != CalendarWorkMode.STUB }
    handle {
        eventResponse = eventRepoDone
        eventsResponse = eventsRepoDone
        state = when (val st = state) {
            CalendarState.RUNNING -> CalendarState.FINISHING
            else -> st
        }
    }
}
