package ru.otus.otuskotlin.calendar.common.repo

import ru.otus.otuskotlin.calendar.common.models.CalendarEvent
import ru.otus.otuskotlin.calendar.common.models.CalendarEventId
import ru.otus.otuskotlin.calendar.common.models.CalendarEventLock

data class DbEventIdRequest(
    val id: CalendarEventId,
    val lock: CalendarEventLock = CalendarEventLock.NONE,
) {
    constructor(event: CalendarEvent): this(event.id, event.lock)
}
