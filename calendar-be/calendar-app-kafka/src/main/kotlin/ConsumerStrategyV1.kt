package ru.otus.otuskotlin.calendar.app.kafka

import ru.otus.otuskotlin.calendar.api.v1.apiV1RequestDeserialize
import ru.otus.otuskotlin.calendar.api.v1.apiV1ResponseSerialize
import ru.otus.otuskotlin.calendar.api.v1.models.IRequest
import ru.otus.otuskotlin.calendar.api.v1.models.IResponse
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.mappers.v1.fromTransport
import ru.otus.otuskotlin.calendar.mappers.v1.toTransportEvent

class ConsumerStrategyV1 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: CalendarContext): String {
        val response: IResponse = source.toTransportEvent()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: CalendarContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}
