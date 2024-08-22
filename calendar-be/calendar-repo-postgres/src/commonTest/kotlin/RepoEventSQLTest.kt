package ru.otus.otuskotlin.calendar.backend.repo.postgresql

import ru.otus.otuskotlin.calendar.backend.repo.tests.*
import ru.otus.otuskotlin.calendar.common.repo.IRepoEvent
import ru.otus.otuskotlin.calendar.repo.common.EventRepoInitialized
import ru.otus.otuskotlin.calendar.repo.common.IRepoEventInitializable
import kotlin.test.AfterTest

private fun IRepoEvent.clear() {
    val pgRepo = (this as EventRepoInitialized).repo as RepoEventSql
    pgRepo.clear()
}

class RepoEventSQLCreateTest : RepoEventCreateTest() {
    override val repo: IRepoEventInitializable = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { uuidNew.asString() },
    )
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoEventSQLReadTest : RepoEventReadTest() {
    override val repo: IRepoEvent = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoEventSQLUpdateTest : RepoEventUpdateTest() {
    override val repo: IRepoEvent = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
    @AfterTest
    fun tearDown() {
        repo.clear()
    }
}

class RepoEventSQLDeleteTest : RepoEventDeleteTest() {
    override val repo: IRepoEvent = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoEventSQLSearchTest : RepoEventSearchTest() {
    override val repo: IRepoEvent = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}
