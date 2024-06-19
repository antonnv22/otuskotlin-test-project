package ru.otus.otuskotlin.calendar.common.ws

interface ICalendarWsSessionRepo {
    fun add(session: ICalendarWsSession)
    fun clearAll()
    fun remove(session: ICalendarWsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : ICalendarWsSessionRepo {
            override fun add(session: ICalendarWsSession) {}
            override fun clearAll() {}
            override fun remove(session: ICalendarWsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}
