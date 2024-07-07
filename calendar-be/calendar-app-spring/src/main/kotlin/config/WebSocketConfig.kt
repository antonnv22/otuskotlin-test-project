package ru.otus.otuskotlin.calendar.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import ru.otus.otuskotlin.calendar.app.spring.controllers.EventControllerV1Ws
import ru.otus.otuskotlin.calendar.app.spring.controllers.EventControllerV2Ws


@Suppress("unused")
@Configuration
class WebSocketConfig(
    private val adControllerV1: EventControllerV1Ws,
    private val adControllerV2: EventControllerV2Ws,
) {
    @Bean
    fun handlerMapping(): HandlerMapping {
        val handlerMap: Map<String, WebSocketHandler> = mapOf(
            "/v1/ws" to adControllerV1,
            "/v2/ws" to adControllerV2,
        )
        return SimpleUrlHandlerMapping(handlerMap, 1)
    }
}
