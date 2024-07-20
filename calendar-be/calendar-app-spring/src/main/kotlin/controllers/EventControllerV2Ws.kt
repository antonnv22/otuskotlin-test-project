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
import ru.otus.otuskotlin.calendar.app.spring.base.CalendarAppSettings
import ru.otus.otuskotlin.calendar.app.spring.base.SpringWsSessionV2
import ru.otus.otuskotlin.calendar.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.calendar.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.calendar.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.calendar.api.v2.mappers.toTransportEvent
import ru.otus.otuskotlin.calendar.api.v2.models.IRequest
import ru.otus.otuskotlin.calendar.app.common.controllerHelper
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.CalendarCommand

@Component
class EventControllerV2Ws(private val appSettings: CalendarAppSettings) : WebSocketHandler {
    private val sessions = appSettings.corSettings.wsSessions

    override fun handle(session: WebSocketSession): Mono<Void> = runBlocking {
        val calendarSess = SpringWsSessionV2(session)
        sessions.add(calendarSess)
        val messageObj = process("ws-v2-init") {
            command = CalendarCommand.INIT
            wsSession = calendarSess
        }

        val messages = session.receive().asFlow()
            .map { message ->
                process("ws-v2-handle") {
                    wsSession = calendarSess
                    val request = apiV2RequestDeserialize<IRequest>(message.payloadAsText)
                    fromTransport(request)
                }
            }

        val output = merge(flowOf(messageObj), messages)
            .onCompletion {
                process("ws-v2-finish") {
                    command = CalendarCommand.FINISH
                    wsSession = calendarSess
                }
                sessions.remove(calendarSess)
            }
            .map { session.textMessage(apiV2ResponseSerialize(it)) }
            .asFlux()
        session.send(output)
    }

    private suspend fun process(logId: String, function: CalendarContext.() -> Unit) = appSettings.controllerHelper(
        getRequest = function,
        toResponse = CalendarContext::toTransportEvent,
        clazz = this@EventControllerV2Ws::class,
        logId = logId,
    )
}
