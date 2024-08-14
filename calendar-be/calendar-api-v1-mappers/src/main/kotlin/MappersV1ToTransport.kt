package ru.otus.otuskotlin.calendar.mappers.v1

import ru.otus.otuskotlin.calendar.api.v1.models.*
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.exceptions.UnknownCalendarCommand
import ru.otus.otuskotlin.calendar.common.models.*

fun CalendarContext.toTransportEvent(): IResponse = when (val cmd = command) {
    CalendarCommand.CREATE -> toTransportCreate()
    CalendarCommand.READ -> toTransportRead()
    CalendarCommand.UPDATE -> toTransportUpdate()
    CalendarCommand.DELETE -> toTransportDelete()
    CalendarCommand.SEARCH -> toTransportSearch()
    CalendarCommand.INIT -> toTransportInit()
    CalendarCommand.FINISH -> object: IResponse {
        override val responseType: String? = null
        override val result: ResponseResult? = null
        override val errors: List<Error>? = null
    }
    CalendarCommand.NONE -> throw UnknownCalendarCommand(cmd)
}

fun CalendarContext.toTransportCreate() = EventCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    event = eventResponse.toTransportEvent()
)

fun CalendarContext.toTransportRead() = EventReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    event = eventResponse.toTransportEvent()
)

fun CalendarContext.toTransportUpdate() = EventUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    event = eventResponse.toTransportEvent()
)

fun CalendarContext.toTransportDelete() = EventDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    event = eventResponse.toTransportEvent()
)

fun CalendarContext.toTransportSearch() = EventSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    events = eventsResponse.toTransportEvent()
)

fun CalendarContext.toTransportInit() = EventInitResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

fun List<CalendarEvent>.toTransportEvent(): List<EventResponseObject>? = this
    .map { it.toTransportEvent() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun CalendarEvent.toTransportEvent(): EventResponseObject = EventResponseObject(
    id = id.takeIf { it != CalendarEventId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    start = start.takeIf { it.isNotBlank() },
    end = end.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != CalendarUserId.NONE }?.asString(),
    visibility = visibility.toTransportEvent(),
    permissions = permissionsClient.toTransportEvent(),
)

private fun Set<CalendarEventPermissionClient>.toTransportEvent(): Set<EventPermissions>? = this
    .map { it.toTransportEvent() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun CalendarEventPermissionClient.toTransportEvent() = when (this) {
    CalendarEventPermissionClient.READ -> EventPermissions.READ
    CalendarEventPermissionClient.UPDATE -> EventPermissions.UPDATE
    CalendarEventPermissionClient.MAKE_VISIBLE_OWNER -> EventPermissions.MAKE_VISIBLE_OWN
    CalendarEventPermissionClient.MAKE_VISIBLE_GROUP -> EventPermissions.MAKE_VISIBLE_GROUP
    CalendarEventPermissionClient.MAKE_VISIBLE_PUBLIC -> EventPermissions.MAKE_VISIBLE_PUBLIC
    CalendarEventPermissionClient.DELETE -> EventPermissions.DELETE
}

private fun CalendarVisibility.toTransportEvent(): EventVisibility ? = when (this) {
    CalendarVisibility.VISIBLE_PUBLIC -> EventVisibility .PUBLIC
    CalendarVisibility.VISIBLE_TO_GROUP -> EventVisibility .REGISTERED_ONLY
    CalendarVisibility.VISIBLE_TO_OWNER -> EventVisibility .OWNER_ONLY
    CalendarVisibility.NONE -> null
}

private fun List<CalendarError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportEvent() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun CalendarError.toTransportEvent() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun CalendarState.toResult(): ResponseResult? = when (this) {
    CalendarState.RUNNING -> ResponseResult.SUCCESS
    CalendarState.FAILING -> ResponseResult.ERROR
    CalendarState.FINISHING -> ResponseResult.SUCCESS
    CalendarState.NONE -> null
}
