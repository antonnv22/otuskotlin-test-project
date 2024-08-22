package ru.otus.otuskotlin.calendar.backend.repo.tests

import ru.otus.otuskotlin.calendar.common.models.*

abstract class BaseInitEvents(private val op: String): IInitObjects<CalendarEvent> {
    open val lockOld: CalendarEventLock = CalendarEventLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: CalendarEventLock = CalendarEventLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        ownerId: CalendarUserId = CalendarUserId("owner-123"),
        lock: CalendarEventLock = lockOld,
    ) = CalendarEvent(
        id = CalendarEventId("event-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        visibility = CalendarVisibility.VISIBLE_TO_OWNER,
        lock = lock,
    )
}
