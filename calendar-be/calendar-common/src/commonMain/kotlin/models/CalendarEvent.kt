package ru.otus.otuskotlin.calendar.common.models

data class CalendarEvent(
    var id: CalendarEventId = CalendarEventId.NONE,
    var title: String = "",
    var description: String = "",
    var start: String = "",
    var end: String = "",
    var ownerId: CalendarUserId = CalendarUserId.NONE,
    var visibility: CalendarVisibility = CalendarVisibility.NONE,
    var lock: CalendarEventLock = CalendarEventLock.NONE,
    val permissionsClient: MutableSet<CalendarEventPermissionClient> = mutableSetOf()
) {
    fun deepCopy(): CalendarEvent = copy(
        permissionsClient = permissionsClient.toMutableSet(),
    )

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = CalendarEvent()
    }
}
