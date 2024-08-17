package ru.otus.otuskotlin.calendar.common.repo

import ru.otus.otuskotlin.calendar.common.helpers.errorSystem
import ru.otus.otuskotlin.calendar.common.models.CalendarEvent
import ru.otus.otuskotlin.calendar.common.models.CalendarEventId
import ru.otus.otuskotlin.calendar.common.models.CalendarEventLock
import ru.otus.otuskotlin.calendar.common.models.CalendarError
import ru.otus.otuskotlin.calendar.common.repo.exceptions.RepoConcurrencyException
import ru.otus.otuskotlin.calendar.common.repo.exceptions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: CalendarEventId) = DbEventResponseErr(
    CalendarError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbEventResponseErr(
    CalendarError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldEvent: CalendarEvent,
    expectedLock: CalendarEventLock,
    exception: Exception = RepoConcurrencyException(
        id = oldEvent.id,
        expectedLock = expectedLock,
        actualLock = oldEvent.lock,
    ),
) = DbEventResponseErrWithData(
    event = oldEvent,
    err = CalendarError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldEvent.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: CalendarEventId) = DbEventResponseErr(
    CalendarError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Event ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbEventResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)
