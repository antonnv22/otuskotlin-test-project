package ru.otus.otuskotlin.calendar.biz.validation

import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings
import ru.otus.otuskotlin.calendar.common.models.CalendarCommand

abstract class BaseBusinessValidationTest {
    protected abstract val command: CalendarCommand
    private val settings by lazy { CalendarCorSettings() }
    protected val processor by lazy { CalendarEventProcessor(settings) }
}
