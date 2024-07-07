package ru.otus.otuskotlin.calendar.app.kafka

import ru.otus.otuskotlin.calendar.common.CalendarContext

/**
 * Интерфейс стратегии для обслуживания версии API
 */
interface IConsumerStrategy {
    /**
     * Топики, для которых применяется стратегия
     */
    fun topics(config: AppKafkaConfig): InputOutputTopics
    /**
     * Сериализатор для версии API
     */
    fun serialize(source: CalendarContext): String
    /**
     * Десериализатор для версии API
     */
    fun deserialize(value: String, target: CalendarContext)
}
