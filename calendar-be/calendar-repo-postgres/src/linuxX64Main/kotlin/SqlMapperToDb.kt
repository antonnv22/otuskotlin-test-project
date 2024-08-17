package ru.otus.otuskotlin.calendar.backend.repo.postgresql

import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.repo.DbEventFilterRequest

private fun String.toDb() = this.takeIf { it.isNotBlank() }

internal fun CalendarEventId.toDb() = mapOf(
    SqlFields.ID to asString().toDb(),
)
internal fun CalendarEventLock.toDb() = mapOf(
    SqlFields.LOCK_OLD to asString().toDb(),
)

internal fun CalendarEvent.toDb() = id.toDb() + mapOf(
    SqlFields.TITLE to title.toDb(),
    SqlFields.DESCRIPTION to description.toDb(),
    SqlFields.OWNER_ID to ownerId.asString().toDb(),
    SqlFields.START to start.asString().toDb(),
    SqlFields.END to end.asString().toDb(),
    SqlFields.VISIBILITY to visibility.toDb(),
    SqlFields.LOCK to lock.asString().toDb(),
)

internal fun DbEventFilterRequest.toDb() = mapOf(
                                                 // Используется для LIKE '%titleFilter%
    SqlFields.FILTER_TITLE to titleFilter.toDb()?.let { "%$it%"},
    SqlFields.FILTER_OWNER_ID to ownerId.asString().toDb(),
)

private fun CalendarVisibility.toDb() = when (this) {
    CalendarVisibility.VISIBLE_TO_OWNER -> SqlFields.VISIBILITY_OWNER
    CalendarVisibility.VISIBLE_TO_GROUP -> SqlFields.VISIBILITY_GROUP
    CalendarVisibility.VISIBLE_PUBLIC -> SqlFields.VISIBILITY_PUBLIC
    CalendarVisibility.NONE -> null
}
