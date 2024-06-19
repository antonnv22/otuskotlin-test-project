package ru.otus.otuskotlin.calendar.logging.jvm

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.calendar.logging.common.ICalendarLogWrapper
import kotlin.reflect.KClass

/**
 * Generate internal CalendarLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun calendarLoggerLogback(logger: Logger): ICalendarLogWrapper = CalendarLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun calendarLoggerLogback(clazz: KClass<*>): ICalendarLogWrapper = calendarLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun calendarLoggerLogback(loggerId: String): ICalendarLogWrapper = calendarLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
