package ru.otus.otuskotlin.calendar.common.repo

import ru.otus.otuskotlin.calendar.common.helpers.errorSystem

abstract class EventRepoBase: IRepoEvent {

    protected suspend fun tryEventMethod(block: suspend () -> IDbEventResponse) = try {
        block()
    } catch (e: Throwable) {
        DbEventResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryEventsMethod(block: suspend () -> IDbEventsResponse) = try {
        block()
    } catch (e: Throwable) {
        DbEventsResponseErr(errorSystem("methodException", e = e))
    }

}
