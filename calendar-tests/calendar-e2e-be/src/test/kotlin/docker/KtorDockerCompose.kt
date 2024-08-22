package ru.otus.otuskotlin.calendar.e2e.be.docker

import ru.otus.otuskotlin.calendar.e2e.be.fixture.docker.AbstractDockerCompose

object KtorDockerCompose : AbstractDockerCompose(
    "app-ktor_1", 8080, "docker-compose-ktor.yml"
)
