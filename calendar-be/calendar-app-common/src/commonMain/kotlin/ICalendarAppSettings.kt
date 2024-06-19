package ru.otus.otuskotlin.calendar.app.common

import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings

interface ICalendarAppSettings {
    val processor: CalendarEventProcessor
    val corSettings: CalendarCorSettings
}
