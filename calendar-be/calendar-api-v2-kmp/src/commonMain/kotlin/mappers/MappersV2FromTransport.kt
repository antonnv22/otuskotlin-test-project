package ru.otus.otuskotlin.calendar.api.v2.mappers

import ru.otus.otuskotlin.calendar.api.v2.models.*
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.models.CalendarWorkMode
import ru.otus.otuskotlin.calendar.common.stubs.CalendarStubs

fun CalendarContext.fromTransport(request: IRequest) = when (request) {
    is EventCreateRequest -> fromTransport(request)
    is EventReadRequest -> fromTransport(request)
    is EventUpdateRequest -> fromTransport(request)
    is EventDeleteRequest -> fromTransport(request)
    is EventSearchRequest -> fromTransport(request)
}

private fun String?.toEventId() = this?.let { CalendarEventId(it) } ?: CalendarEventId.NONE
private fun String?.toEventLock() = this?.let { CalendarEventLock(it) } ?: CalendarEventLock.NONE
private fun EventReadObject?.toInternal() = if (this != null) {
    CalendarEvent(id = id.toEventId())
} else {
    CalendarEvent()
}

private fun EventDebug?.transportToWorkMode(): CalendarWorkMode = when (this?.mode) {
    EventRequestDebugMode.PROD -> CalendarWorkMode.PROD
    EventRequestDebugMode.TEST -> CalendarWorkMode.TEST
    EventRequestDebugMode.STUB -> CalendarWorkMode.STUB
    null -> CalendarWorkMode.PROD
}

private fun EventDebug?.transportToStubCase(): CalendarStubs = when (this?.stub) {
    EventRequestDebugStubs.SUCCESS -> CalendarStubs.SUCCESS
    EventRequestDebugStubs.NOT_FOUND -> CalendarStubs.NOT_FOUND
    EventRequestDebugStubs.BAD_ID -> CalendarStubs.BAD_ID
    EventRequestDebugStubs.BAD_TITLE -> CalendarStubs.BAD_TITLE
    EventRequestDebugStubs.BAD_DESCRIPTION -> CalendarStubs.BAD_DESCRIPTION
    EventRequestDebugStubs.BAD_VISIBILITY -> CalendarStubs.BAD_VISIBILITY
    EventRequestDebugStubs.CANNOT_DELETE -> CalendarStubs.CANNOT_DELETE
    EventRequestDebugStubs.BAD_SEARCH_STRING -> CalendarStubs.BAD_SEARCH_STRING
    null -> CalendarStubs.NONE
}

fun CalendarContext.fromTransport(request: EventCreateRequest) {
    command = CalendarCommand.CREATE
    eventRequest = request.event?.toInternal() ?: CalendarEvent()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun CalendarContext.fromTransport(request: EventReadRequest) {
    command = CalendarCommand.READ
    eventRequest = request.event.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun CalendarContext.fromTransport(request: EventUpdateRequest) {
    command = CalendarCommand.UPDATE
    eventRequest = request.event?.toInternal() ?: CalendarEvent()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun CalendarContext.fromTransport(request: EventDeleteRequest) {
    command = CalendarCommand.DELETE
    eventRequest = request.event.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun EventDeleteObject?.toInternal(): CalendarEvent = if (this != null) {
    CalendarEvent(
        id = id.toEventId(),
        lock = lock.toEventLock(),
    )
} else {
    CalendarEvent()
}

fun CalendarContext.fromTransport(request: EventSearchRequest) {
    command = CalendarCommand.SEARCH
    eventFilterRequest = request.eventFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun EventSearchFilter?.toInternal(): CalendarEventFilter = CalendarEventFilter(
    searchString = this?.searchString ?: ""
)

private fun EventCreateObject.toInternal(): CalendarEvent = CalendarEvent(
    title = this.title ?: "",
    description = this.description ?: "",
    visibility = this.visibility.fromTransport(),
)

private fun EventUpdateObject.toInternal(): CalendarEvent = CalendarEvent(
    id = this.id.toEventId(),
    title = this.title ?: "",
    description = this.description ?: "",
    visibility = this.visibility.fromTransport(),
    lock = this.lock.toEventLock(),
)

private fun EventVisibility?.fromTransport(): CalendarVisibility = when (this) {
    EventVisibility.PUBLIC -> CalendarVisibility.VISIBLE_PUBLIC
    EventVisibility.OWNER_ONLY -> CalendarVisibility.VISIBLE_TO_OWNER
    EventVisibility.REGISTERED_ONLY -> CalendarVisibility.VISIBLE_TO_GROUP
    null -> CalendarVisibility.NONE
}
