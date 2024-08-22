package ru.otus.otuskotlin.calendar.common.repo.exceptions

import ru.otus.otuskotlin.calendar.common.models.CalendarEventId

class RepoEmptyLockException(id: CalendarEventId): RepoEventException(
    id,
    "Lock is empty in DB"
)
