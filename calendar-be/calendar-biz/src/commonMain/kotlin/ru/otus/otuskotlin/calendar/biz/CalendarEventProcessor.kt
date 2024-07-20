package ru.otus.otuskotlin.calendar.biz

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.stubs.CalendarEventStub

@Suppress("unused", "RedundantSuspendModifier")
class CalendarEventProcessor(val corSettings: CalendarCorSettings) {
    suspend fun exec(ctx: CalendarContext) {
        ctx.eventResponse = CalendarEventStub.get()
        ctx.eventsResponse = CalendarEventStub.prepareSearchList("event search").toMutableList()
        ctx.state = CalendarState.RUNNING
    }
}
