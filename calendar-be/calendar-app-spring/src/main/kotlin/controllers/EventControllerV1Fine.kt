package ru.otus.otuskotlin.calendar.app.spring.controllers

import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.calendar.app.spring.base.CalendarAppSettings
import ru.otus.otuskotlin.calendar.api.v1.models.*
import ru.otus.otuskotlin.calendar.app.common.controllerHelper
import ru.otus.otuskotlin.calendar.mappers.v1.fromTransport
import ru.otus.otuskotlin.calendar.mappers.v1.toTransportEvent
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v1/event")
class EventControllerV1Fine(
    private val appSettings: CalendarAppSettings
) {

    @PostMapping("create")
    suspend fun create(@RequestBody request: EventCreateRequest): EventCreateResponse =
        process(appSettings, request = request, this::class, "create")

    @PostMapping("read")
    suspend fun  read(@RequestBody request: EventReadRequest): EventReadResponse =
        process(appSettings, request = request, this::class, "read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun  update(@RequestBody request: EventUpdateRequest): EventUpdateResponse =
        process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun  delete(@RequestBody request: EventDeleteRequest): EventDeleteResponse =
        process(appSettings, request = request, this::class, "delete")

    @PostMapping("search")
    suspend fun  search(@RequestBody request: EventSearchRequest): EventSearchResponse =
        process(appSettings, request = request, this::class, "search")

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: CalendarAppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            {
                fromTransport(request)
            },
            { toTransportEvent() as R },
            clazz,
            logId,
        )
    }
}
