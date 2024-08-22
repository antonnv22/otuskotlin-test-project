package ru.otus.otuskotlin.calendar.e2e.be.docker

import ru.otus.otuskotlin.calendar.e2e.be.fixture.docker.AbstractDockerCompose

object KafkaDockerCompose : AbstractDockerCompose(
    "kafka_1", 9091, "docker-compose-kafka.yml"
)
