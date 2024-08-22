package ru.otus.otuskotlin.calendar.common.repo

import ru.otus.otuskotlin.calendar.common.models.CalendarEvent
import ru.otus.otuskotlin.calendar.common.models.CalendarError

sealed interface IDbEventResponse: IDbResponse<CalendarEvent>

data class DbEventResponseOk(
    val data: CalendarEvent
): IDbEventResponse

data class DbEventResponseErr(
    val errors: List<CalendarError> = emptyList()
): IDbEventResponse {
    constructor(err: CalendarError): this(listOf(err))
}

data class DbEventResponseErrWithData(
    val data: CalendarEvent,
    val errors: List<CalendarError> = emptyList()
): IDbEventResponse {
    constructor(event: CalendarEvent, err: CalendarError): this(event, listOf(err))
}
