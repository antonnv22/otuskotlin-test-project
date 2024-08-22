package ru.otus.otuskotlin.calendar.common

import ru.otus.otuskotlin.calendar.common.repo.IRepoEvent
import ru.otus.otuskotlin.calendar.common.ws.ICalendarWsSessionRepo
import ru.otus.otuskotlin.calendar.logging.common.CalendarLoggerProvider

data class CalendarCorSettings(
    val loggerProvider: CalendarLoggerProvider = CalendarLoggerProvider(),
    val wsSessions: ICalendarWsSessionRepo = ICalendarWsSessionRepo.NONE,
    val repoStub: IRepoEvent = IRepoEvent.NONE,
    val repoTest: IRepoEvent = IRepoEvent.NONE,
    val repoProd: IRepoEvent = IRepoEvent.NONE,
) {
    companion object {
        val NONE = CalendarCorSettings()
    }
}
