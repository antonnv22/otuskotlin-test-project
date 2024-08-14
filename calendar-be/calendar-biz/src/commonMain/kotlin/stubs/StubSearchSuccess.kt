package ru.otus.otuskotlin.calendar.biz.stubs

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.common.stubs.CalendarStubs
import ru.otus.otuskotlin.calendar.cor.ICorChainDsl
import ru.otus.otuskotlin.calendar.cor.worker
import ru.otus.otuskotlin.calendar.logging.common.LogLevel
import ru.otus.otuskotlin.calendar.stubs.CalendarEventStub

fun ICorChainDsl<CalendarContext>.stubSearchSuccess(title: String, corSettings: CalendarCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для поиска событий
    """.trimIndent()
    on { stubCase == CalendarStubs.SUCCESS && state == CalendarState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubSearchSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = CalendarState.FINISHING
            eventsResponse.addAll(CalendarEventStub.prepareSearchList(eventFilterRequest.searchString))
        }
    }
}
