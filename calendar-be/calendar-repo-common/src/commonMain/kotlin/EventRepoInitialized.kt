package ru.otus.otuskotlin.calendar.repo.common

import ru.otus.otuskotlin.calendar.common.models.CalendarEvent

class EventRepoInitialized(
    val repo: IRepoEventInitializable,
    initObjects: Collection<CalendarEvent> = emptyList(),
) : IRepoEventInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<CalendarEvent> = save(initObjects).toList()
}
