package ru.otus.otuskotlin.calendar.common.repo

interface IRepoEvent {
    suspend fun createEvent(rq: DbEventRequest): IDbEventResponse
    suspend fun readEvent(rq: DbEventIdRequest): IDbEventResponse
    suspend fun updateEvent(rq: DbEventRequest): IDbEventResponse
    suspend fun deleteEvent(rq: DbEventIdRequest): IDbEventResponse
    suspend fun searchEvent(rq: DbEventFilterRequest): IDbEventsResponse
    companion object {
        val NONE = object : IRepoEvent {
            override suspend fun createEvent(rq: DbEventRequest): IDbEventResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readEvent(rq: DbEventIdRequest): IDbEventResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateEvent(rq: DbEventRequest): IDbEventResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteEvent(rq: DbEventIdRequest): IDbEventResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchEvent(rq: DbEventFilterRequest): IDbEventsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
