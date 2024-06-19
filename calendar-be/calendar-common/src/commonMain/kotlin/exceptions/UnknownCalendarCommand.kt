package ru.otus.otuskotlin.calendar.common.exceptions

import ru.otus.otuskotlin.calendar.common.models.CalendarCommand


class UnknownCalendarCommand(command: CalendarCommand) : Throwable("Wrong command $command at mapping toTransport stage")
