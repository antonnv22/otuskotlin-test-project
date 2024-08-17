package ru.otus.otuskotlin.calendar.biz.repo

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.helpers.fail
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.common.repo.errorRepoConcurrency
import ru.otus.otuskotlin.calendar.cor.ICorChainDsl
import ru.otus.otuskotlin.calendar.cor.worker

fun ICorChainDsl<CalendarContext>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == CalendarState.RUNNING && eventValidated.lock != eventRepoRead.lock }
    handle {
        fail(errorRepoConcurrency(eventRepoRead, eventValidated.lock).errors)
    }
}
