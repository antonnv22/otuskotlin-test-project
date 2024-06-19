package ru.otus.otuskotlin.calendar.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class CalendarEventLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CalendarEventLock("")
    }
}
