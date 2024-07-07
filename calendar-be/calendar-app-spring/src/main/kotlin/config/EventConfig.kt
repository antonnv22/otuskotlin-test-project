package ru.otus.otuskotlin.calendar.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.calendar.app.spring.base.CalendarAppSettings
import ru.otus.otuskotlin.calendar.app.spring.base.SpringWsSessionRepo
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings
import ru.otus.otuskotlin.calendar.logging.common.CalendarLoggerProvider
import ru.otus.otuskotlin.calendar.logging.jvm.calendarLoggerLogback

@Suppress("unused")
@Configuration
class EventConfig {
    @Bean
    fun processor(corSettings: CalendarCorSettings) = CalendarEventProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): CalendarLoggerProvider = CalendarLoggerProvider { calendarLoggerLogback(it) }

    @Bean
    fun corSettings(): CalendarCorSettings = CalendarCorSettings(
        loggerProvider = loggerProvider(),
        wsSessions = wsRepo(),
    )

    @Bean
    fun appSettings(
        corSettings: CalendarCorSettings,
        processor: CalendarEventProcessor,
    ) = CalendarAppSettings(
        corSettings = corSettings,
        processor = processor,
    )

    @Bean
    fun wsRepo(): SpringWsSessionRepo = SpringWsSessionRepo()

}
