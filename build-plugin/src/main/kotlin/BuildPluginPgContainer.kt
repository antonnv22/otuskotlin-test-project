package ru.otus.otuskotlin.calendar.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait

@Suppress("unused")
internal class BuildPluginPgContainer : Plugin<Project> {
    val pgDbName = "calendar_events"
    val pgUsername = "postgres"
    val pgPassword = "calendar-pass"

    private val pgContainer = PostgreSQLContainer<Nothing>("postgres:latest").apply {
        withUsername(pgUsername)
        withPassword(pgPassword)
        withDatabaseName(pgDbName)
        waitingFor(Wait.forLogMessage("database system is ready to accept connections", 1))
    }


    override fun apply(project: Project): Unit = with(project) {
        val stopTask = tasks.register("pgStop") {
            group = "containers"
            pgContainer.stop()
        }
        tasks.register("pgStart", PgContainerStartTask::class.java) {
            pgContainer.start()
            pgUrl = pgContainer.jdbcUrl
            finalizedBy(stopTask)
        }
    }
}
