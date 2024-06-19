package ru.otus.otuskotlin.calendar.common.models

data class CalendarEventFilter(
    var searchString: String = "",
    var ownerId: CalendarUserId = CalendarUserId.NONE,
)
