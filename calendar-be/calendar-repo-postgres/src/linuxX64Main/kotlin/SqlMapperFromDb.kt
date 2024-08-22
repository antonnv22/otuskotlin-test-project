package ru.otus.otuskotlin.calendar.backend.repo.postgresql

import io.github.moreirasantos.pgkn.resultset.ResultSet
import ru.otus.otuskotlin.calendar.common.models.*

internal fun ResultSet.fromDb(cols: List<String>): CalendarEvent {
    val fieldsMap = cols.mapIndexed { i: Int, field: String -> field to i }.toMap()
    fun col(field: String): String? = fieldsMap[field]?.let { getString(it) }
    return CalendarEvent(
        id = col(SqlFields.ID)?.let { CalendarEventId(it) } ?: CalendarEventId.NONE,
        title = col(SqlFields.TITLE) ?: "",
        description = col(SqlFields.DESCRIPTION) ?: "",
        start = col(SqlFields.START) ?: "",
        end = col(SqlFields.END) ?: "",
        ownerId = col(SqlFields.OWNER_ID)?.let { CalendarUserId(it) } ?: CalendarUserId.NONE,
        visibility = col(SqlFields.VISIBILITY).asVisibility(),
        lock = col(SqlFields.LOCK)?.let { CalendarEventLock(it) } ?: CalendarEventLock.NONE,
    )
}

private fun String?.asVisibility(): CalendarVisibility = when (this) {
    SqlFields.VISIBILITY_OWNER -> CalendarVisibility.VISIBLE_TO_OWNER
    SqlFields.VISIBILITY_GROUP -> CalendarVisibility.VISIBLE_TO_GROUP
    SqlFields.VISIBILITY_PUBLIC -> CalendarVisibility.VISIBLE_PUBLIC
    else -> CalendarVisibility.NONE
}
