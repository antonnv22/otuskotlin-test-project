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

fun ICorChainDsl<CalendarContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление события из БД по ID"
    on { state == CalendarState.RUNNING }
    handle {
        val request = DbEventIdRequest(eventRepoPrepare)
        when(val result = eventRepo.deleteEvent(request)) {
            is DbEventResponseOk -> eventRepoDone = result.data
            is DbEventResponseErr -> {
                fail(result.errors)
                eventRepoDone = eventRepoRead
            }
            is DbEventResponseErrWithData -> {
                fail(result.errors)
                eventRepoDone = result.data
            }
        }
    }
}
