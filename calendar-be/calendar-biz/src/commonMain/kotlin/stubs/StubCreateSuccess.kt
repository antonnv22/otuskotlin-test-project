package ru.otus.otuskotlin.calendar.biz.stubs

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.common.models.CalendarVisibility
import ru.otus.otuskotlin.calendar.common.stubs.CalendarStubs
import ru.otus.otuskotlin.calendar.cor.ICorChainDsl
import ru.otus.otuskotlin.calendar.cor.worker
import ru.otus.otuskotlin.calendar.logging.common.LogLevel
import ru.otus.otuskotlin.calendar.stubs.CalendarEventStub

fun ICorChainDsl<CalendarContext>.stubCreateSuccess(title: String, corSettings: CalendarCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для создания события
    """.trimIndent()
    on { stubCase == CalendarStubs.SUCCESS && state == CalendarState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubCreateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = CalendarState.FINISHING
            val stub = CalendarEventStub.prepareResult {
                eventRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                eventRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
                eventRequest.start.takeIf { it.isNotBlank() }?.also { this.start = it }
                eventRequest.end.takeIf { it.isNotBlank() }?.also { this.end = it }
                eventRequest.visibility.takeIf { it != CalendarVisibility.NONE }?.also { this.visibility = it }
            }
            eventResponse = stub
        }
    }
}
