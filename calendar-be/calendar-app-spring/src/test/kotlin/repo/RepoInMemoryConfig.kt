package ru.otus.otuskotlin.calendar.app.spring.repo

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import ru.otus.otuskotlin.calendar.common.repo.IRepoEvent
import ru.otus.otuskotlin.calendar.repo.inmemory.EventRepoInMemory

@TestConfiguration
class RepoInMemoryConfig {
    @Suppress("unused")
    @Bean()
    @Primary
    fun prodRepo(): IRepoEvent = EventRepoInMemory()
}
