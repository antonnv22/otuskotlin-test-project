package ru.otus.otuskotlin.calendar.common.ws

interface ICalendarWsSession {
    suspend fun <T> send(obj: T)
    companion object {
        val NONE = object : ICalendarWsSession {
            override suspend fun <T> send(obj: T) {

            }
        }
    }
}
