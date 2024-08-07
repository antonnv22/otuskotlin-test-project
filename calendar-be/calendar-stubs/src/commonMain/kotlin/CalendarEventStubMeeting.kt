package ru.otus.otuskotlin.calendar.stubs

import ru.otus.otuskotlin.calendar.common.models.*

object CalendarEventStubMeeting {
    val EVENT_1: CalendarEvent
        get() = CalendarEvent(
            id = CalendarEventId("777"),
            title = "Дейли",
            description = "Ежедневное дейли",
            ownerId = CalendarUserId("user-1"),
            visibility = CalendarVisibility.VISIBLE_PUBLIC,
            permissionsClient = mutableSetOf(
                CalendarEventPermissionClient.READ,
                CalendarEventPermissionClient.UPDATE,
                CalendarEventPermissionClient.DELETE,
                CalendarEventPermissionClient.MAKE_VISIBLE_PUBLIC,
                CalendarEventPermissionClient.MAKE_VISIBLE_GROUP,
                CalendarEventPermissionClient.MAKE_VISIBLE_OWNER,
            )
        )
    val EVENT_2 = EVENT_1.copy()
}
