package ru.otus.otuskotlin.calendar.repo.common

import ru.otus.otuskotlin.calendar.common.models.CalendarEvent
import ru.otus.otuskotlin.calendar.common.repo.IRepoEvent

interface IRepoEventInitializable : IRepoEvent {
    fun save(events: Collection<CalendarEvent>): Collection<CalendarEvent>
}
