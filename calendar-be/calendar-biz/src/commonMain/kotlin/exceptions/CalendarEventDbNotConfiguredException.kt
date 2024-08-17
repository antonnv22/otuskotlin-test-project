package ru.otus.otuskotlin.calendar.biz.exceptions

import ru.otus.otuskotlin.calendar.common.models.CalendarWorkMode

class CalendarEventDbNotConfiguredException(val workMode: CalendarWorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)
