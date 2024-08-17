package ru.otus.otuskotlin.calendar.biz.repo

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.cor.ICorChainDsl
import ru.otus.otuskotlin.calendar.cor.worker

fun ICorChainDsl<CalendarContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = """
        Готовим данные к удалению из БД
    """.trimIndent()
    on { state == CalendarState.RUNNING }
    handle {
        eventRepoPrepare = eventValidated.deepCopy()
    }
}
