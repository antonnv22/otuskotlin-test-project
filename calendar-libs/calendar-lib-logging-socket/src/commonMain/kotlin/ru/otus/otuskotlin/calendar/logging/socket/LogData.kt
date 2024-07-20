package ru.otus.otuskotlin.calendar.logging.socket

import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.calendar.logging.common.LogLevel

@Serializable
data class LogData(
    val level: LogLevel,
    val message: String,
//    val data: T
)
