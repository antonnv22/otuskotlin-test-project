package ru.otus.otuskotlin.calendar.app.spring.base

import ru.otus.otuskotlin.calendar.app.common.ICalendarAppSettings
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings

data class CalendarAppSettings(
    override val corSettings: CalendarCorSettings,
    override val processor: CalendarEventProcessor,
): ICalendarAppSettings
