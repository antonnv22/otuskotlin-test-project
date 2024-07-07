package ru.otus.otuskotlin.calendar.app.spring.controllers

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import ru.otus.otuskotlin.calendar.api.v1.apiV1Mapper
import ru.otus.otuskotlin.calendar.api.v1.models.IRequest
import ru.otus.otuskotlin.calendar.app.common.controllerHelper
import ru.otus.otuskotlin.calendar.app.spring.base.CalendarAppSettings
import ru.otus.otuskotlin.calendar.app.spring.base.SpringWsSessionV1
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.CalendarCommand
import ru.otus.otuskotlin.calendar.mappers.v1.fromTransport
import ru.otus.otuskotlin.calendar.mappers.v1.toTransportEvent

@Component
class EventControllerV1Ws(private val appSettings: CalendarAppSettings) : WebSocketHandler {
    private val sessions = appSettings.corSettings.wsSessions

    override fun handle(session: WebSocketSession): Mono<Void> = runBlocking {
//        // Обслуживаем INIT логику
//
//        // Получаем поток входящих сообщений
//        val input = session.receive()
//        // Формируем поток исходящих сообщений
//        val output1 = input
//            .map {session.textMessage("Echo $it")}
//            .doOnComplete {
//                // Можно выполнить логику FINISH
//            }
//            .onErrorComplete {
//                // Обработка ошибок
//                true
//            }
//        return@runBlocking session.send(output1)


        val calendarSess = SpringWsSessionV1(session)
        sessions.add(calendarSess)
        val messageObj = process("ws-v1-init") {
            command = CalendarCommand.INIT
            wsSession = calendarSess
        }
        calendarSess.send(messageObj)

        val messages = session.receive().asFlow()
            .map { message ->
                process("ws-v1-handle") {
                    wsSession = calendarSess
                    val request = apiV1Mapper.readValue(message.payloadAsText, IRequest::class.java)
                    fromTransport(request)
                }
            }

        val output = merge(flowOf(messageObj), messages)
            .onCompletion {
                process("ws-v1-finish") {
                    wsSession = calendarSess
                    command = CalendarCommand.FINISH
                }
                sessions.remove(calendarSess)
            }
            .map { session.textMessage(apiV1Mapper.writeValueAsString(it)) }
            .asFlux()
        session.send(output)
    }

    private suspend fun process(logId: String, function: CalendarContext.() -> Unit) = appSettings.controllerHelper(
        getRequest = function,
        toResponse = CalendarContext::toTransportEvent,
        clazz = this@EventControllerV1Ws::class,
        logId = logId,
    )
}
