package ru.otus.otuskotlin.calendar.app.spring.repo

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.slot
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import ru.otus.otuskotlin.calendar.app.spring.config.EventConfig
import ru.otus.otuskotlin.calendar.app.spring.controllers.EventControllerV1Fine
import ru.otus.otuskotlin.calendar.common.repo.DbEventFilterRequest
import ru.otus.otuskotlin.calendar.common.repo.DbEventIdRequest
import ru.otus.otuskotlin.calendar.common.repo.DbEventRequest
import ru.otus.otuskotlin.calendar.common.repo.IRepoEvent
import ru.otus.otuskotlin.calendar.repo.common.EventRepoInitialized
import ru.otus.otuskotlin.calendar.repo.inmemory.EventRepoInMemory
import ru.otus.otuskotlin.calendar.stubs.CalendarEventStub
import kotlin.test.Test

@WebFluxTest(
    EventControllerV1Fine::class, EventConfig::class,
    properties = ["spring.main.allow-bean-definition-overriding=true"]
)
@Import(RepoInMemoryConfig::class)
internal class EventRepoInMemoryV1Test : EventRepoBaseV1Test() {

    @Autowired
    override lateinit var webClient: WebTestClient

    @MockkBean
    @Qualifier("testRepo")
    lateinit var testTestRepo: IRepoEvent

    @BeforeEach
    fun tearUp() {
        val slotEvent = slot<DbEventRequest>()
        val slotId = slot<DbEventIdRequest>()
        val slotFl = slot<DbEventFilterRequest>()
        val repo = EventRepoInitialized(
            repo = EventRepoInMemory(randomUuid = { uuidNew }),
            initObjects = CalendarEventStub.prepareSearchList("xx") + CalendarEventStub.get()
        )
        coEvery { testTestRepo.createEvent(capture(slotEvent)) } coAnswers { repo.createEvent(slotEvent.captured) }
        coEvery { testTestRepo.readEvent(capture(slotId)) } coAnswers { repo.readEvent(slotId.captured) }
        coEvery { testTestRepo.updateEvent(capture(slotEvent)) } coAnswers { repo.updateEvent(slotEvent.captured) }
        coEvery { testTestRepo.deleteEvent(capture(slotId)) } coAnswers { repo.deleteEvent(slotId.captured) }
        coEvery { testTestRepo.searchEvent(capture(slotFl)) } coAnswers { repo.searchEvent(slotFl.captured) }
    }

    @Test
    override fun createEvent() = super.createEvent()

    @Test
    override fun readEvent() = super.readEvent()

    @Test
    override fun updateEvent() = super.updateEvent()

    @Test
    override fun deleteEvent() = super.deleteEvent()

    @Test
    override fun searchEvent() = super.searchEvent()
}
