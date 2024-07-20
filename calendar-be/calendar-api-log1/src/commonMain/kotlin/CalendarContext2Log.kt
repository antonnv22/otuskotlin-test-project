package ru.otus.otuskotlin.calendar.api.log1.mapper

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.calendar.api.log1.models.*
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.api.log1.models.CommonLogModel

fun CalendarContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "calendar",
    event = toCalendarLog(),
    errors = errors.map { it.toLog() },
)

private fun CalendarContext.toCalendarLog(): CalendarLogModel? {
    val adNone = CalendarEvent()
    return CalendarLogModel(
        requestId = requestId.takeIf { it != CalendarRequestId.NONE }?.asString(),
        requestEvent = eventRequest.takeIf { it != adNone }?.toLog(),
        responseEvent = eventResponse.takeIf { it != adNone }?.toLog(),
        responseEvents = eventsResponse.takeIf { it.isNotEmpty() }?.filter { it != adNone }?.map { it.toLog() },
        requestFilter = eventFilterRequest.takeIf { it != CalendarEventFilter() }?.toLog(),
    ).takeIf { it != CalendarLogModel() }
}

private fun CalendarEventFilter.toLog() = EventFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != CalendarUserId.NONE }?.asString(),
)

private fun CalendarError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun CalendarEvent.toLog() = EventLog(
    id = id.takeIf { it != CalendarEventId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    visibility = visibility.takeIf { it != CalendarVisibility.NONE }?.name,
    ownerId = ownerId.takeIf { it != CalendarUserId.NONE }?.asString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)
