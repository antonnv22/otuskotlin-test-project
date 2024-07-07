package ru.otus.otuskotlin.calendar.app.kafka

import ru.otus.otuskotlin.calendar.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.calendar.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.calendar.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.calendar.api.v2.mappers.toTransportEvent
import ru.otus.otuskotlin.calendar.api.v2.models.IRequest
import ru.otus.otuskotlin.calendar.api.v2.models.IResponse
import ru.otus.otuskotlin.calendar.common.CalendarContext

class ConsumerStrategyV2 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV2, config.kafkaTopicOutV2)
    }

    override fun serialize(source: CalendarContext): String {
        val response: IResponse = source.toTransportEvent()
        return apiV2ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: CalendarContext) {
        val request: IRequest = apiV2RequestDeserialize(value)
        target.fromTransport(request)
    }
}
