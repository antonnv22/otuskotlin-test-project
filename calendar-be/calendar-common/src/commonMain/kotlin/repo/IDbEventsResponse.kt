package ru.otus.otuskotlin.calendar.common.repo

import ru.otus.otuskotlin.calendar.common.models.CalendarEvent
import ru.otus.otuskotlin.calendar.common.models.CalendarError

sealed interface IDbEventsResponse: IDbResponse<List<CalendarEvent>>

data class DbEventsResponseOk(
    val data: List<CalendarEvent>
): IDbEventsResponse

@Suppress("unused")
data class DbEventsResponseErr(
    val errors: List<CalendarError> = emptyList()
): IDbEventsResponse {
    constructor(err: CalendarError): this(listOf(err))
}
