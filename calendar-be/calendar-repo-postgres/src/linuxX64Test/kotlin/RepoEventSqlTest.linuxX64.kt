package ru.otus.otuskotlin.calendar.backend.repo.postgresql

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.test.runTest
import platform.posix.getenv
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.repo.DbEventFilterRequest
import ru.otus.otuskotlin.calendar.common.repo.DbEventRequest
import kotlin.test.Test

class RepoEventSqlTest {
    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun create() = runTest {
        val pgPort = getenv("postgresPort")?.toKString()?.toIntOrNull() ?: 5432

        val repo = RepoEventSql(
            properties = SqlProperties(port = pgPort)
        )
        val res = repo.createEvent(
            rq = DbEventRequest(
                CalendarEvent(
                    title = "tttt",
                    description = "zzzz",
                    visibility = CalendarVisibility.VISIBLE_PUBLIC,
                    start = "2024-08-20T20:38:41.927Z",
                    end = "2024-08-20T21:38:41.927Z",
                    ownerId = CalendarUserId("1234"),
                    lock = CalendarEventLock("235356"),
                )
            )
        )
        println("CREATED $res")
    }

    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun search() = runTest {
        val pgPort = getenv("postgresPort")?.toKString()?.toIntOrNull() ?: 5432

        val repo = RepoEventSql(
            properties = SqlProperties(port = pgPort)
        )
        val res = repo.searchEvent(
            rq = DbEventFilterRequest(
                titleFilter = "tttt",
                ownerId = CalendarUserId("1234"),
            )
        )
        println("SEARCH $res")
    }
}
