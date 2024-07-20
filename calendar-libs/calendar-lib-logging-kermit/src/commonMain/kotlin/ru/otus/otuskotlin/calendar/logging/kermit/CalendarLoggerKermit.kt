package ru.otus.otuskotlin.calendar.logging.kermit

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import ru.otus.otuskotlin.calendar.logging.common.ICalendarLogWrapper
import kotlin.reflect.KClass

@Suppress("unused")
fun calendarLoggerKermit(loggerId: String): ICalendarLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return CalendarLoggerWrapperKermit(
        logger = logger,
        loggerId = loggerId,
    )
}

@Suppress("unused")
fun calendarLoggerKermit(cls: KClass<*>): ICalendarLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return CalendarLoggerWrapperKermit(
        logger = logger,
        loggerId = cls.qualifiedName?: "",
    )
}
