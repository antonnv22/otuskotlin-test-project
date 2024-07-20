package ru.otus.otuskotlin.calendar.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class CalendarUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CalendarUserId("")
    }
}
