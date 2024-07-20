package ru.otus.otuskotlin.calendar.common.helpers

import ru.otus.otuskotlin.calendar.common.models.CalendarError

fun Throwable.asCalendarError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = CalendarError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
