package ru.otus.otuskotlin.calendar.app.spring.config

import org.springframework.boot.context.properties.ConfigurationProperties
import ru.otus.otuskotlin.calendar.backend.repo.postgresql.SqlProperties

@ConfigurationProperties(prefix = "psql")
data class EventConfigPostgres(
    var host: String = "localhost",
    var port: Int = 5432,
    var user: String = "postgres",
    var password: String = "calendar-pass",
    var database: String = "calendar_events",
    var schema: String = "public",
    var table: String = "events",
) {
    val psql: SqlProperties = SqlProperties(
        host = host,
        port = port,
        user = user,
        password = password,
        database = database,
        schema = schema,
        table = table,
    )
}
