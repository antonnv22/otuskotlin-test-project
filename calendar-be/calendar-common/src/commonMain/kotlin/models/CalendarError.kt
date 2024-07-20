package ru.otus.otuskotlin.calendar.common.models

import ru.otus.otuskotlin.calendar.logging.common.LogLevel


data class CalendarError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)
