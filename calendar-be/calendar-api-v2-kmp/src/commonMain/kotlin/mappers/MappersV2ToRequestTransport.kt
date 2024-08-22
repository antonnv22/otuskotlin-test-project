package ru.otus.otuskotlin.calendar.api.v2.mappers

import ru.otus.otuskotlin.calendar.api.v2.models.*
import ru.otus.otuskotlin.calendar.common.models.*

fun CalendarEvent.toTransportCreate() = EventCreateObject(
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    start = start.takeIf { it.isNotBlank() },
    end = end.takeIf { it.isNotBlank() },
    visibility = visibility.toTransportEvent(),
)

fun CalendarEvent.toTransportRead() = EventReadObject(
    id = id.takeIf { it != CalendarEventId.NONE }?.asString(),
)

fun CalendarEvent.toTransportUpdate() = EventUpdateObject(
    id = id.takeIf { it != CalendarEventId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    start = start.takeIf { it.isNotBlank() },
    end = end.takeIf { it.isNotBlank() },
    visibility = visibility.toTransportEvent(),
    lock = lock.takeIf { it != CalendarEventLock.NONE }?.asString(),
)

fun CalendarEvent.toTransportDelete() = EventDeleteObject(
    id = id.takeIf { it != CalendarEventId.NONE }?.asString(),
    lock = lock.takeIf { it != CalendarEventLock.NONE }?.asString(),
)
