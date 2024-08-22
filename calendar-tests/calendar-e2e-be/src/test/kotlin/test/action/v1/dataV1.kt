package ru.otus.otuskotlin.calendar.e2e.be.test.action.v1

import ru.otus.otuskotlin.calendar.api.v1.models.*
import ru.otus.otuskotlin.calendar.e2e.be.test.TestDebug

val debugStubV1 = EventDebug(mode = EventRequestDebugMode.STUB, stub = EventRequestDebugStubs.SUCCESS)

val someCreateEvent = EventCreateObject(
    title = "Ретро",
    description = "Ретро после спринта",
    start = "2024-08-23T20:00:00Z",
    end = "2024-08-23T20:30:00Z",
    visibility = EventVisibility.PUBLIC
)

fun TestDebug.toV1() = when(this) {
    TestDebug.STUB -> debugStubV1
    TestDebug.PROD -> EventDebug(mode = EventRequestDebugMode.PROD)
    TestDebug.TEST -> EventDebug(mode = EventRequestDebugMode.TEST)
}
