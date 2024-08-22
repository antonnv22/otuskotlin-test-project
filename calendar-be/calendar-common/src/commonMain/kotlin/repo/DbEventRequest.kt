package ru.otus.otuskotlin.calendar.common.repo

import ru.otus.otuskotlin.calendar.common.models.CalendarEvent

data class DbEventRequest(
    val event: CalendarEvent
)
