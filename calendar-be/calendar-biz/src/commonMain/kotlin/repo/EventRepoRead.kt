package ru.otus.otuskotlin.calendar.biz.repo

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.helpers.fail
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.common.repo.DbEventIdRequest
import ru.otus.otuskotlin.calendar.common.repo.DbEventResponseErr
import ru.otus.otuskotlin.calendar.common.repo.DbEventResponseErrWithData
import ru.otus.otuskotlin.calendar.common.repo.DbEventResponseOk
import ru.otus.otuskotlin.calendar.cor.ICorChainDsl
import ru.otus.otuskotlin.calendar.cor.worker

fun ICorChainDsl<CalendarContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение события из БД"
    on { state == CalendarState.RUNNING }
    handle {
        val request = DbEventIdRequest(eventValidated)
        when(val result = eventRepo.readEvent(request)) {
            is DbEventResponseOk -> eventRepoRead = result.data
            is DbEventResponseErr -> fail(result.errors)
            is DbEventResponseErrWithData -> {
                fail(result.errors)
                eventRepoRead = result.data
            }
        }
    }
}
