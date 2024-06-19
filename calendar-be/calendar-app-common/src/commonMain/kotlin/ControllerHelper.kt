package ru.otus.otuskotlin.calendar.app.common

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.calendar.api.log1.mapper.toLog
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.helpers.asCalendarError
import ru.otus.otuskotlin.calendar.common.models.CalendarCommand
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import kotlin.reflect.KClass

suspend inline fun <T> ICalendarAppSettings.controllerHelper(
    crossinline getRequest: suspend CalendarContext.() -> Unit,
    crossinline toResponse: suspend CalendarContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = CalendarContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId),
            e = e,
        )
        ctx.state = CalendarState.FAILING
        ctx.errors.add(e.asCalendarError())
        processor.exec(ctx)
        if (ctx.command == CalendarCommand.NONE) {
            ctx.command = CalendarCommand.READ
        }
        ctx.toResponse()
    }
}
