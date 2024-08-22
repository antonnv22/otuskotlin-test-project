package ru.otus.otuskotlin.calendar.app.spring.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.calendar.app.spring.base.CalendarAppSettings
import ru.otus.otuskotlin.calendar.app.spring.base.SpringWsSessionRepo
import ru.otus.otuskotlin.calendar.backend.repo.postgresql.RepoEventSql
import ru.otus.otuskotlin.calendar.backend.repository.inmemory.EventRepoStub
import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings
import ru.otus.otuskotlin.calendar.common.repo.IRepoEvent
import ru.otus.otuskotlin.calendar.logging.common.CalendarLoggerProvider
import ru.otus.otuskotlin.calendar.logging.jvm.calendarLoggerLogback
import ru.otus.otuskotlin.calendar.repo.inmemory.EventRepoInMemory

@Suppress("unused")
@EnableConfigurationProperties(EventConfigPostgres::class)
@Configuration
class EventConfig(val postgresConfig: EventConfigPostgres) {

    val logger: Logger = LoggerFactory.getLogger(EventConfig::class.java)

    @Bean
    fun processor(corSettings: CalendarCorSettings) = CalendarEventProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): CalendarLoggerProvider = CalendarLoggerProvider { calendarLoggerLogback(it) }

    @Bean
    fun testRepo(): IRepoEvent = EventRepoInMemory()

    @Bean
    fun prodRepo(): IRepoEvent = RepoEventSql(postgresConfig.psql).apply {
        logger.info("Connecting to DB with ${this}")
    }

    @Bean
    fun stubRepo(): IRepoEvent = EventRepoStub()


    @Bean
    fun corSettings(): CalendarCorSettings = CalendarCorSettings(
        loggerProvider = loggerProvider(),
        wsSessions = wsRepo(),
        repoTest = testRepo(),
        repoProd = prodRepo(),
        repoStub = stubRepo(),
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
