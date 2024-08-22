package ru.otus.otuskotlin.calendar.backend.repo.postgresql

import io.github.moreirasantos.pgkn.PostgresDriver
import io.github.moreirasantos.pgkn.resultset.ResultSet
import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.calendar.backend.repo.postgresql.SqlFields.quoted
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.repo.*
import ru.otus.otuskotlin.calendar.repo.common.IRepoEventInitializable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoEventSql actual constructor(
    properties: SqlProperties,
    val randomUuid: () -> String,
) : IRepoEvent, IRepoEventInitializable {
    init {
        require(properties.database.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL database must contain only letters, numbers and underscore symbol '_'"
        }
        require(properties.schema.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL schema must contain only letters, numbers and underscore symbol '_'"
        }
        require(properties.table.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL table must contain only letters, numbers and underscore symbol '_'"
        }
    }

    private val dbName: String = "\"${properties.schema}\".\"${properties.table}\"".apply {
    }
    init {
        initConnection(properties)
    }

    private suspend fun saveElement(saveEvent: CalendarEvent): IDbEventResponse {
        val sql = """
                INSERT INTO $dbName (
                  ${SqlFields.ID.quoted()}, 
                  ${SqlFields.TITLE.quoted()}, 
                  ${SqlFields.DESCRIPTION.quoted()},
                  ${SqlFields.VISIBILITY.quoted()},
                  ${SqlFields.START.quoted()},
                  ${SqlFields.END.quoted()},
                  ${SqlFields.LOCK.quoted()},
                  ${SqlFields.OWNER_ID.quoted()},
                ) VALUES (
                  :${SqlFields.ID}, 
                  :${SqlFields.TITLE}, 
                  :${SqlFields.DESCRIPTION},
                  :${SqlFields.START},
                  :${SqlFields.END},
                  :${SqlFields.VISIBILITY}::${SqlFields.VISIBILITY_TYPE},
                  :${SqlFields.LOCK}, 
                  :${SqlFields.OWNER_ID}, 
                )
                RETURNING ${SqlFields.allFields.joinToString()}
            """.trimIndent()
        val res = driver.execute(
            sql = sql,
            saveEvent.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return DbEventResponseOk(res.first())
    }

    actual override fun save(events: Collection<CalendarEvent>): Collection<CalendarEvent> = runBlocking {
        events.map {
            val res = saveElement(it)
            if (res !is DbEventResponseOk) throw Exception()
            res.data
        }
    }

    actual override suspend fun createEvent(rq: DbEventRequest): IDbEventResponse {
        val saveEvent = rq.event.copy(id = CalendarEventId(randomUuid()), lock = CalendarEventLock(randomUuid()))
        return saveElement(saveEvent)
    }

    actual override suspend fun readEvent(rq: DbEventIdRequest): IDbEventResponse {
        val sql = """
                SELECT ${SqlFields.allFields.joinToString { it.quoted() }}
                FROM $dbName
                WHERE ${SqlFields.ID.quoted()} = :${SqlFields.ID}
            """.trimIndent()
        val res = driver.execute(
            sql = sql,
            rq.id.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return if (res.isEmpty()) errorNotFound(rq.id) else DbEventResponseOk(res.first())
    }

    actual override suspend fun updateEvent(rq: DbEventRequest): IDbEventResponse {
        val sql = """
            WITH update_obj AS (
                UPDATE $dbName a
                SET ${SqlFields.TITLE.quoted()} = :${SqlFields.TITLE}
                , ${SqlFields.DESCRIPTION.quoted()} = :${SqlFields.DESCRIPTION}
                , ${SqlFields.VISIBILITY.quoted()} = :${SqlFields.VISIBILITY}::${SqlFields.VISIBILITY_TYPE}
                , ${SqlFields.START.quoted()} = :${SqlFields.START}
                , ${SqlFields.END.quoted()} = :${SqlFields.END}
                , ${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK}
                , ${SqlFields.OWNER_ID.quoted()} = :${SqlFields.OWNER_ID}
                WHERE  a.${SqlFields.ID.quoted()} = :${SqlFields.ID}
                AND a.${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK_OLD}
                RETURNING ${SqlFields.allFields.joinToString()}
            ),
            select_obj AS (
                SELECT ${SqlFields.allFields.joinToString()} FROM $dbName 
                WHERE ${SqlFields.ID.quoted()} = :${SqlFields.ID} 
            )
            (SELECT * FROM update_obj UNION ALL SELECT * FROM select_obj) LIMIT 1
        """.trimIndent()
        val rqEvent = rq.event
        val newEvent = rqEvent.copy(lock = CalendarEventLock(randomUuid()))
        val res = driver.execute(
            sql = sql,
            newEvent.toDb() + rqEvent.lock.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        val returnedEvent: CalendarEvent? = res.firstOrNull()
        return when {
            returnedEvent == null -> errorNotFound(rq.event.id)
            returnedEvent.lock == newEvent.lock -> DbEventResponseOk(returnedEvent)
            else -> errorRepoConcurrency(returnedEvent, rqEvent.lock)
        }
    }

    actual override suspend fun deleteEvent(rq: DbEventIdRequest): IDbEventResponse {
        val sql = """
            WITH delete_obj AS (
                DELETE FROM $dbName a
                WHERE  a.${SqlFields.ID.quoted()} = :${SqlFields.ID}
                AND a.${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK_OLD}
                RETURNING '${SqlFields.DELETE_OK}'
            )
            SELECT ${SqlFields.allFields.joinToString()}, (SELECT * FROM delete_obj) as flag FROM $dbName 
            WHERE ${SqlFields.ID.quoted()} = :${SqlFields.ID} 
        """.trimIndent()
        val res = driver.execute(
            sql = sql,
            rq.id.toDb() + rq.lock.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) to row.getString(SqlFields.allFields.size) }
        val returnedPair: Pair<CalendarEvent,String?>? = res.firstOrNull()
        val returnedEvent: CalendarEvent? = returnedPair?.first
        return when {
            returnedEvent == null -> errorNotFound(rq.id)
            returnedPair.second == SqlFields.DELETE_OK -> DbEventResponseOk(returnedEvent)
            else -> errorRepoConcurrency(returnedEvent, rq.lock)
        }
    }

    actual override suspend fun searchEvent(rq: DbEventFilterRequest): IDbEventsResponse {
        val where = listOfNotNull(
            rq.ownerId.takeIf { it != CalendarUserId.NONE }
                ?.let { "${SqlFields.OWNER_ID.quoted()} = :${SqlFields.OWNER_ID}" },
            rq.titleFilter.takeIf { it.isNotBlank() }
                ?.let { "${SqlFields.TITLE.quoted()} LIKE :${SqlFields.TITLE}" },
        )
            .takeIf { it.isNotEmpty() }
            ?.let { "WHERE ${it.joinToString(separator = " AND ")}" }
            ?: ""

        val sql = """
                SELECT ${SqlFields.allFields.joinToString { it.quoted() }}
                FROM $dbName $where
            """.trimIndent()
        println("SQL: $sql")
        val res = driver.execute(
            sql = sql,
            rq.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return DbEventsResponseOk(res)
    }

    actual fun clear(): Unit = runBlocking {
        val sql = """
                DELETE FROM $dbName 
            """.trimIndent()
        driver.execute(sql = sql)
    }

    companion object {
        private lateinit var driver: PostgresDriver
        private fun initConnection(properties: SqlProperties) {
            if (!this::driver.isInitialized) {
                driver = PostgresDriver(
                    host = properties.host,
                    port = properties.port,
                    user = properties.user,
                    database = properties.database,
                    password = properties.password,
                )
            }
        }
    }
}
