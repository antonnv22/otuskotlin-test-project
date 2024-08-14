package ru.otus.otuskotlin.calendar.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.stubs.CalendarStubs
import ru.otus.otuskotlin.calendar.common.ws.ICalendarWsSession

data class CalendarContext(
    var command: CalendarCommand = CalendarCommand.NONE,
    var state: CalendarState = CalendarState.NONE,
    val errors: MutableList<CalendarError> = mutableListOf(),

    var corSettings: CalendarCorSettings = CalendarCorSettings(),
    var workMode: CalendarWorkMode = CalendarWorkMode.PROD,
    var stubCase: CalendarStubs = CalendarStubs.NONE,
    var wsSession: ICalendarWsSession = ICalendarWsSession.NONE,

    var requestId: CalendarRequestId = CalendarRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var eventRequest: CalendarEvent = CalendarEvent(),
    var eventFilterRequest: CalendarEventFilter = CalendarEventFilter(),

    var eventValidating: CalendarEvent = CalendarEvent(),
    var eventFilterValidating: CalendarEventFilter = CalendarEventFilter(),

    var eventValidated: CalendarEvent = CalendarEvent(),
    var eventFilterValidated: CalendarEventFilter = CalendarEventFilter(),

    var eventResponse: CalendarEvent = CalendarEvent(),
    var eventsResponse: MutableList<CalendarEvent> = mutableListOf(),

    )
