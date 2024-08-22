package ru.otus.otuskotlin.calendar.common.repo

import ru.otus.otuskotlin.calendar.common.models.CalendarUserId

data class DbEventFilterRequest(
    val titleFilter: String = "",
    val ownerId: CalendarUserId = CalendarUserId.NONE,
)
