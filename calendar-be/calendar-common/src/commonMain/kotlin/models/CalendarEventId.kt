package ru.otus.otuskotlin.calendar.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class CalendarEventId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CalendarEventId("")
    }
}
