package ru.otus.otuskotlin.calendar.backend.repo.postgresql

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.otus.otuskotlin.calendar.common.models.*

class EventTable(tableName: String) : Table(tableName) {
    val id = text(SqlFields.ID)
    val title = text(SqlFields.TITLE).nullable()
    val description = text(SqlFields.DESCRIPTION).nullable()
    val owner = text(SqlFields.OWNER_ID)
    val start = text(SqlFields.START)
    val end = text(SqlFields.END)
    val visibility = visibilityEnumeration(SqlFields.VISIBILITY)
    val lock = text(SqlFields.LOCK)

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = CalendarEvent(
        id = CalendarEventId(res[id].toString()),
        title = res[title] ?: "",
        description = res[description] ?: "",
        ownerId = CalendarUserId(res[owner].toString()),
        start = res[start] ?: "",
        end = res[end] ?: "",
        visibility = res[visibility],
        lock = CalendarEventLock(res[lock]),
    )

    fun to(it: UpdateBuilder<*>, event: CalendarEvent, randomUuid: () -> String) {
        it[id] = event.id.takeIf { it != CalendarEventId.NONE }?.asString() ?: randomUuid()
        it[title] = event.title
        it[description] = event.description
        it[owner] = event.ownerId.asString()
        it[start] = event.start
        it[end] = event.end
        it[visibility] = event.visibility
        it[lock] = event.lock.takeIf { it != CalendarEventLock.NONE }?.asString() ?: randomUuid()
    }
}

