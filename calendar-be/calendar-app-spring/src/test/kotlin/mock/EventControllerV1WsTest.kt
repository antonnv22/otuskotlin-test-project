package ru.otus.otuskotlin.calendar.app.spring.mock

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import ru.otus.otuskotlin.calendar.api.v1.apiV1RequestSerialize
import ru.otus.otuskotlin.calendar.api.v1.apiV1ResponseDeserialize
import ru.otus.otuskotlin.calendar.api.v1.models.*
import kotlin.test.Test


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Suppress("unused")
class EventControllerV1WsTest : EventControllerBaseWsTest<IRequest, IResponse>("v1") {

    @LocalServerPort
    override var port: Int = 0

    override fun deserializeRs(response: String): IResponse = apiV1ResponseDeserialize(response)
    override fun serializeRq(request: IRequest): String = apiV1RequestSerialize(request)

    @Test
    fun wsCreate(): Unit = testWsApp(
        EventCreateRequest(
            debug = EventDebug(EventRequestDebugMode.STUB, EventRequestDebugStubs.SUCCESS),
            event = EventCreateObject(
                title = "test1",
                description = "desc",
                visibility = EventVisibility.PUBLIC,
            )
        )
    ) { pl ->
        val mesInit = pl[0]
        val mesCreate = pl[1]
        assert(mesInit is EventInitResponse)
        assert(mesInit.result == ResponseResult.SUCCESS)
        assert(mesCreate is EventCreateResponse)
        assert(mesCreate.result == ResponseResult.SUCCESS)
    }
}
