package ru.otus.otuskotlin.calendar.common

import ru.otus.otuskotlin.calendar.common.ws.ICalendarWsSessionRepo
import ru.otus.otuskotlin.calendar.logging.common.CalendarLoggerProvider

data class CalendarCorSettings(
    val loggerProvider: CalendarLoggerProvider = CalendarLoggerProvider(),
    val wsSessions: ICalendarWsSessionRepo = ICalendarWsSessionRepo.NONE,
) {
    companion object {
        val NONE = CalendarCorSettings()
    }
}
