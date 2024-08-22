import ru.otus.otuskotlin.calendar.backend.repo.tests.*
import ru.otus.otuskotlin.calendar.repo.common.EventRepoInitialized
import ru.otus.otuskotlin.calendar.repo.inmemory.EventRepoInMemory

class EventRepoInMemoryCreateTest : RepoEventCreateTest() {
    override val repo = EventRepoInitialized(
        EventRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class EventRepoInMemoryDeleteTest : RepoEventDeleteTest() {
    override val repo = EventRepoInitialized(
        EventRepoInMemory(),
        initObjects = initObjects,
    )
}

class EventRepoInMemoryReadTest : RepoEventReadTest() {
    override val repo = EventRepoInitialized(
        EventRepoInMemory(),
        initObjects = initObjects,
    )
}

class EventRepoInMemorySearchTest : RepoEventSearchTest() {
    override val repo = EventRepoInitialized(
        EventRepoInMemory(),
        initObjects = initObjects,
    )
}

class EventRepoInMemoryUpdateTest : RepoEventUpdateTest() {
    override val repo = EventRepoInitialized(
        EventRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}
