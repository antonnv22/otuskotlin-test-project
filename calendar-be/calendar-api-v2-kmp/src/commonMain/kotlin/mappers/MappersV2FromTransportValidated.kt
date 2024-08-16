package ru.otus.otuskotlin.calendar.api.v2.mappers

import ru.otus.otuskotlin.calendar.api.v2.models.*
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.stubs.CalendarStubs

private sealed interface Result<T,E>
private data class Ok<T,E>(val value: T) : Result<T,E>
private data class Err<T,E>(val errors: List<E>) : Result<T,E> {
    constructor(error: E) : this(listOf(error))
}

private fun <T,E> Result<T,E>.getOrExec(default: T, block: (Err<T,E>) -> Unit = {}): T = when (this) {
    is Ok<T,E> -> this.value
    is Err<T,E> -> {
        block(this)
        default
    }
}

@Suppress("unused")
private fun <T,E> Result<T,E>.getOrNull(block: (Err<T,E>) -> Unit = {}): T? = when (this) {
    is Ok<T,E> -> this.value
    is Err<T,E> -> {
        block(this)
        null
    }
}

private fun String?.transportToStubCaseValidated(): Result<CalendarStubs,CalendarError> = when (this) {
    "success" -> Ok(CalendarStubs.SUCCESS)
    "notFound" -> Ok(CalendarStubs.NOT_FOUND)
    "badId" -> Ok(CalendarStubs.BAD_ID)
    "badTitle" -> Ok(CalendarStubs.BAD_TITLE)
    "badDescription" -> Ok(CalendarStubs.BAD_DESCRIPTION)
    "badVisibility" -> Ok(CalendarStubs.BAD_VISIBILITY)
    "cannotDelete" -> Ok(CalendarStubs.CANNOT_DELETE)
    "badSearchString" -> Ok(CalendarStubs.BAD_SEARCH_STRING)
    null -> Ok(CalendarStubs.NONE)
    else -> Err(
        CalendarError(
            code = "wrong-stub-case",
            group = "mapper-validation",
            field = "debug.stub",
            message = "Unsupported value for case \"$this\""
        )
    )
}

@Suppress("unused")
fun CalendarContext.fromTransportValidated(request: EventCreateRequest) {
    command = CalendarCommand.CREATE
    stubCase = request
        .debug
        ?.stub
        ?.value
        .transportToStubCaseValidated()
        .getOrExec(CalendarStubs.NONE) { err: Err<CalendarStubs,CalendarError> ->
            errors.addAll(err.errors)
            state = CalendarState.FAILING
        }
}