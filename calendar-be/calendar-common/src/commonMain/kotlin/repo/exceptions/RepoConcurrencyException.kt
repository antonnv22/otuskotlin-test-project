package ru.otus.otuskotlin.calendar.common.repo.exceptions

import ru.otus.otuskotlin.calendar.common.models.CalendarEventId
import ru.otus.otuskotlin.calendar.common.models.CalendarEventLock

class RepoConcurrencyException(id: CalendarEventId, expectedLock: CalendarEventLock, actualLock: CalendarEventLock?): RepoEventException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
