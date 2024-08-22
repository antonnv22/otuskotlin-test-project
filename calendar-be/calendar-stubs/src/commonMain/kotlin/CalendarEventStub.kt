package ru.otus.otuskotlin.calendar.stubs

import ru.otus.otuskotlin.calendar.common.models.CalendarEvent
import ru.otus.otuskotlin.calendar.common.models.CalendarEventId
import ru.otus.otuskotlin.calendar.common.models.CalendarUserId
import ru.otus.otuskotlin.calendar.stubs.CalendarEventStubMeeting.EVENT_1

object CalendarEventStub {
    fun get(): CalendarEvent = EVENT_1.copy()

    fun prepareResult(block: CalendarEvent.() -> Unit): CalendarEvent = get().apply(block)

    fun prepareSearchList(filter: String) = listOf(
        calendarEventDemand("ev-01", filter),
        calendarEventDemand("ev-02", filter),
        calendarEventDemand("ev-03", filter),
        calendarEventDemand("ev-04", filter),
        calendarEventDemand("ev-05", filter),
        calendarEventDemand("ev-06", filter),
    )

    private fun calendarEventDemand(id: String, filter: String) =
        calendarEvent(EVENT_1, id = id, filter = filter)

    private fun calendarEvent(base: CalendarEvent, id: String, filter: String) = base.copy(
        id = CalendarEventId(id),
        title = "$filter $id",
        description = "desc $filter $id",
    )

}
