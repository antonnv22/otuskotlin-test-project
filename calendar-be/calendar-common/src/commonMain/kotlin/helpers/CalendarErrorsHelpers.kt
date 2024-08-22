package ru.otus.otuskotlin.calendar.common.helpers

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.CalendarError
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.logging.common.LogLevel

fun Throwable.asCalendarError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = CalendarError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

inline fun CalendarContext.addError(error: CalendarError) = errors.add(error)
inline fun CalendarContext.addErrors(error: Collection<CalendarError>) = errors.addAll(error)

inline fun CalendarContext.fail(error: CalendarError) {
    addError(error)
    state = CalendarState.FAILING
}

inline fun CalendarContext.fail(errors: Collection<CalendarError>) {
    addErrors(errors)
    state = CalendarState.FAILING
}

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = CalendarError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)


inline fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = CalendarError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)
