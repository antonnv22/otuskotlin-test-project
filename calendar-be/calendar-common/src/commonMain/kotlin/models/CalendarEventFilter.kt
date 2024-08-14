package ru.otus.otuskotlin.calendar.common.models

data class CalendarEventFilter(
    var searchString: String = "",
    var ownerId: CalendarUserId = CalendarUserId.NONE,
){
    fun deepCopy(): CalendarEventFilter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = CalendarEventFilter()
    }
}
