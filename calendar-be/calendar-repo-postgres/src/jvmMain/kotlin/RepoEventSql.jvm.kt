package ru.otus.otuskotlin.calendar.backend.repo.postgresql

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.otuskotlin.calendar.common.helpers.asCalendarError
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.repo.*
import ru.otus.otuskotlin.calendar.repo.common.IRepoEventInitializable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoEventSql actual constructor(
    properties: SqlProperties,
    private val randomUuid: () -> String
) : IRepoEvent, IRepoEventInitializable {
    private val eventTable = EventTable("${properties.schema}.${properties.table}")

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    actual fun clear(): Unit = transaction(conn) {
        eventTable.deleteAll()
    }

    private fun saveObj(event: CalendarEvent): CalendarEvent = transaction(conn) {
        val res = eventTable
            .insert {
                to(it, event, randomUuid)
            }
            .resultedValues
            ?.map { eventTable.from(it) }
        res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private suspend inline fun <T> transactionWrapper(crossinline block: () -> T, crossinline handle: (Exception) -> T): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbEventResponse): IDbEventResponse =
        transactionWrapper(block) { DbEventResponseErr(it.asCalendarError()) }

    actual override fun save(events: Collection<CalendarEvent>): Collection<CalendarEvent> = events.map { saveObj(it) }
    actual override suspend fun createEvent(rq: DbEventRequest): IDbEventResponse = transactionWrapper {
        DbEventResponseOk(saveObj(rq.event))
    }

    private fun read(id: CalendarEventId): IDbEventResponse {
        val res = eventTable.selectAll().where {
            eventTable.id eq id.asString()
        }.singleOrNull() ?: return errorNotFound(id)
        return DbEventResponseOk(eventTable.from(res))
    }

    actual override suspend fun readEvent(rq: DbEventIdRequest): IDbEventResponse = transactionWrapper { read(rq.id) }

    private suspend fun update(
        id: CalendarEventId,
        lock: CalendarEventLock,
        block: (CalendarEvent) -> IDbEventResponse
    ): IDbEventResponse =
        transactionWrapper {
            if (id == CalendarEventId.NONE) return@transactionWrapper errorEmptyId

            val current = eventTable.selectAll().where { eventTable.id eq id.asString() }
                .singleOrNull()
                ?.let { eventTable.from(it) }

            when {
                current == null -> errorNotFound(id)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }


    actual override suspend fun updateEvent(rq: DbEventRequest): IDbEventResponse = update(rq.event.id, rq.event.lock) {
        eventTable.update({ eventTable.id eq rq.event.id.asString() }) {
            to(it, rq.event.copy(lock = CalendarEventLock(randomUuid())), randomUuid)
        }
        read(rq.event.id)
    }

    actual override suspend fun deleteEvent(rq: DbEventIdRequest): IDbEventResponse = update(rq.id, rq.lock) {
        eventTable.deleteWhere { id eq rq.id.asString() }
        DbEventResponseOk(it)
    }

    actual override suspend fun searchEvent(rq: DbEventFilterRequest): IDbEventsResponse =
        transactionWrapper({
            val res = eventTable.selectAll().where {
                buildList {
                    add(Op.TRUE)
                    if (rq.ownerId != CalendarUserId.NONE) {
                        add(eventTable.owner eq rq.ownerId.asString())
                    }
                    if (rq.titleFilter.isNotBlank()) {
                        add(
                            (eventTable.title like "%${rq.titleFilter}%")
                                    or (eventTable.description like "%${rq.titleFilter}%")
                        )
                    }
                }.reduce { a, b -> a and b }
            }
            DbEventsResponseOk(data = res.map { eventTable.from(it) })
        }, {
            DbEventsResponseErr(it.asCalendarError())
        })
}
