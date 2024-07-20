package ru.otus.otuskotlin.calendar.app.spring.base

import ru.otus.otuskotlin.calendar.common.ws.ICalendarWsSession
import ru.otus.otuskotlin.calendar.common.ws.ICalendarWsSessionRepo

class SpringWsSessionRepo: ICalendarWsSessionRepo {
    private val sessions: MutableSet<ICalendarWsSession> = mutableSetOf()
    override fun add(session: ICalendarWsSession) {
        sessions.add(session)
    }

    override fun clearAll() {
        sessions.clear()
    }

    override fun remove(session: ICalendarWsSession) {
        sessions.remove(session)
    }

    override suspend fun <T> sendAll(obj: T) {
        sessions.forEach { it.send(obj) }
    }
}
