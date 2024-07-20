package ru.otus.otuskotlin.calendar.app.kafka

import ru.otus.otuskotlin.calendar.app.common.ICalendarAppSettings
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings
import ru.otus.otuskotlin.calendar.logging.common.CalendarLoggerProvider
import ru.otus.otuskotlin.calendar.logging.jvm.calendarLoggerLogback

class AppKafkaConfig(
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val kafkaTopicInV1: String = KAFKA_TOPIC_IN_V1,
    val kafkaTopicOutV1: String = KAFKA_TOPIC_OUT_V1,
    val kafkaTopicInV2: String = KAFKA_TOPIC_IN_V2,
    val kafkaTopicOutV2: String = KAFKA_TOPIC_OUT_V2,
    override val corSettings: CalendarCorSettings = CalendarCorSettings(
        loggerProvider = CalendarLoggerProvider { calendarLoggerLogback(it) }
    ),
    override val processor: CalendarEventProcessor = CalendarEventProcessor(corSettings),
): ICalendarAppSettings {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_V1_VAR = "KAFKA_TOPIC_IN_V1"
        const val KAFKA_TOPIC_OUT_V1_VAR = "KAFKA_TOPIC_OUT_V1"
        const val KAFKA_TOPIC_IN_V2_VAR = "KAFKA_TOPIC_IN_V2"
        const val KAFKA_TOPIC_OUT_V2_VAR = "KAFKA_TOPIC_OUT_V2"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_VAR) ?: "").split("\\s*[,; ]\\s*") }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "calendar" }
        val KAFKA_TOPIC_IN_V1 by lazy { System.getenv(KAFKA_TOPIC_IN_V1_VAR) ?: "calendar-event-v1-in" }
        val KAFKA_TOPIC_OUT_V1 by lazy { System.getenv(KAFKA_TOPIC_OUT_V1_VAR) ?: "calendar-event-v1-out" }
        val KAFKA_TOPIC_IN_V2 by lazy { System.getenv(KAFKA_TOPIC_IN_V2_VAR) ?: "calendar-event-v2-in" }
        val KAFKA_TOPIC_OUT_V2 by lazy { System.getenv(KAFKA_TOPIC_OUT_V2_VAR) ?: "calendar-event-v2-out" }
    }
}
