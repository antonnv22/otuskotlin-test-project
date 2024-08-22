package ru.otus.otuskotlin.calendar.backend.repo.postgresql

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "calendar-pass",
    val database: String = "calendar_events",
    val schema: String = "public",
    val table: String = "events",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}
