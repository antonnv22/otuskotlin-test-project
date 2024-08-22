package ru.otus.otuskotlin.calendar.repo.inmemory

import ru.otus.otuskotlin.calendar.common.models.*

data class EventEntity(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    var start: String? = null,
    var end: String? = null,
    val ownerId: String? = null,
    val visibility: String? = null,
    val lock: String? = null,
) {
    constructor(model: CalendarEvent) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        start = model.start.takeIf { it.isNotBlank() },
        end = model.end.takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        visibility = model.visibility.takeIf { it != CalendarVisibility.NONE }?.name,
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = CalendarEvent(
        id = id?.let { CalendarEventId(it) } ?: CalendarEventId.NONE,
        title = title ?: "",
        description = description ?: "",
        start = start ?: "",
        end = end ?: "",
        ownerId = ownerId?.let { CalendarUserId(it) } ?: CalendarUserId.NONE,
        visibility = visibility?.let { CalendarVisibility.valueOf(it) } ?: CalendarVisibility.NONE,
        lock = lock?.let { CalendarEventLock(it) } ?: CalendarEventLock.NONE,
    )
}
