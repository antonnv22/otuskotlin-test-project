package ru.otus.otuskotlin.calendar.e2e.be.docker

import ru.otus.otuskotlin.calendar.e2e.be.fixture.docker.AbstractDockerCompose

object KtorLinuxPGDockerCompose : AbstractDockerCompose(
    "app-ktor_1", 8080, "docker-compose-ktor-pg-linux.yml"
)
