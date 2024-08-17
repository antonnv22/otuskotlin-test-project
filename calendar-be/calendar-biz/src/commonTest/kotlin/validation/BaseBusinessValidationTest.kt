package ru.otus.otuskotlin.calendar.biz.validation

import ru.otus.otuskotlin.calendar.biz.CalendarEventProcessor
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings
import ru.otus.otuskotlin.calendar.common.models.CalendarCommand
import ru.otus.otuskotlin.calendar.repo.common.EventRepoInitialized
import ru.otus.otuskotlin.calendar.repo.inmemory.EventRepoInMemory
import ru.otus.otuskotlin.calendar.stubs.CalendarEventStub

abstract class BaseBusinessValidationTest {
    protected abstract val command: CalendarCommand
    private val repo = EventRepoInitialized(
        repo = EventRepoInMemory(),
        initObjects = listOf(
            CalendarEventStub.get(),
        ),
    )
    private val settings by lazy { CalendarCorSettings(repoTest = repo) }
    protected val processor by lazy { CalendarEventProcessor(settings) }
}
