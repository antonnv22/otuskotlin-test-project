package ru.otus.otuskotlin.calendar.biz.repo

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.helpers.fail
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.common.repo.DbEventRequest
import ru.otus.otuskotlin.calendar.common.repo.DbEventResponseErr
import ru.otus.otuskotlin.calendar.common.repo.DbEventResponseErrWithData
import ru.otus.otuskotlin.calendar.common.repo.DbEventResponseOk
import ru.otus.otuskotlin.calendar.cor.ICorChainDsl
import ru.otus.otuskotlin.calendar.cor.worker

fun ICorChainDsl<CalendarContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == CalendarState.RUNNING }
    handle {
        val request = DbEventRequest(eventRepoPrepare)
        when(val result = eventRepo.updateEvent(request)) {
            is DbEventResponseOk -> eventRepoDone = result.data
            is DbEventResponseErr -> fail(result.errors)
            is DbEventResponseErrWithData -> {
                fail(result.errors)
                eventRepoDone = result.data
            }
        }
    }
}
