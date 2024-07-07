package ru.otus.otuskotlin.calendar.app.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import ru.otus.otuskotlin.calendar.api.v1.apiV1RequestSerialize
import ru.otus.otuskotlin.calendar.api.v1.apiV1ResponseDeserialize
import ru.otus.otuskotlin.calendar.api.v1.models.EventCreateObject
import ru.otus.otuskotlin.calendar.api.v1.models.EventCreateRequest
import ru.otus.otuskotlin.calendar.api.v1.models.EventCreateResponse
import ru.otus.otuskotlin.calendar.api.v1.models.EventDebug
import ru.otus.otuskotlin.calendar.api.v1.models.EventRequestDebugMode
import ru.otus.otuskotlin.calendar.api.v1.models.EventRequestDebugStubs
import ru.otus.otuskotlin.calendar.api.v1.models.EventVisibility
import java.util.*
import kotlin.test.assertEquals


class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        EventCreateRequest(
                            event = EventCreateObject(
                                title = "Дейли",
                                description = "some testing event to check them all",
                                visibility = EventVisibility.OWNER_ONLY,
                            ),
                            debug = EventDebug(
                                mode = EventRequestDebugMode.STUB,
                                stub = EventRequestDebugStubs.SUCCESS,
                            ),
                        ),
                    )
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<EventCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("Дейли", result.event?.title)
    }

    companion object {
        const val PARTITION = 0
    }
}


