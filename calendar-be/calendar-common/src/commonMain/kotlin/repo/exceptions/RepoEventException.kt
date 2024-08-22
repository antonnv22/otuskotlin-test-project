package ru.otus.otuskotlin.calendar.common.repo.exceptions

import ru.otus.otuskotlin.calendar.common.models.CalendarEventId

open class RepoEventException(
    @Suppress("unused")
    val eventId: CalendarEventId,
    msg: String,
): RepoException(msg)
