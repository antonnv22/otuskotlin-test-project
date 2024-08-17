package ru.otus.otuskotlin.calendar.biz.repo

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.helpers.fail
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.common.repo.DbEventFilterRequest
import ru.otus.otuskotlin.calendar.common.repo.DbEventsResponseErr
import ru.otus.otuskotlin.calendar.common.repo.DbEventsResponseOk
import ru.otus.otuskotlin.calendar.cor.ICorChainDsl
import ru.otus.otuskotlin.calendar.cor.worker

fun ICorChainDsl<CalendarContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск событий в БД по фильтру"
    on { state == CalendarState.RUNNING }
    handle {
        val request = DbEventFilterRequest(
            titleFilter = eventFilterValidated.searchString,
            ownerId = eventFilterValidated.ownerId,
        )
        when(val result = eventRepo.searchEvent(request)) {
            is DbEventsResponseOk -> eventsRepoDone = result.data.toMutableList()
            is DbEventsResponseErr -> fail(result.errors)
        }
    }
}
