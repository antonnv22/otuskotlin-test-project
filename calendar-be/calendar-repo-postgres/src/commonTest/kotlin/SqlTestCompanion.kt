package ru.otus.otuskotlin.calendar.backend.repo.postgresql

import com.benasher44.uuid.uuid4
import ru.otus.otuskotlin.calendar.common.models.CalendarEvent
import ru.otus.otuskotlin.calendar.repo.common.EventRepoInitialized
import ru.otus.otuskotlin.calendar.repo.common.IRepoEventInitializable

object SqlTestCompanion {
    private const val HOST = "localhost"
    private const val USER = "postgres"
    private const val PASS = "calendar-pass"
    val PORT = getEnv("postgresPort")?.toIntOrNull() ?: 5432

    fun repoUnderTestContainer(
        initObjects: Collection<CalendarEvent> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): IRepoEventInitializable = EventRepoInitialized(
        repo = RepoEventSql(
            SqlProperties(
                host = HOST,
                user = USER,
                password = PASS,
                port = PORT,
            ),
            randomUuid = randomUuid
        ),
        initObjects = initObjects,
    )
}

