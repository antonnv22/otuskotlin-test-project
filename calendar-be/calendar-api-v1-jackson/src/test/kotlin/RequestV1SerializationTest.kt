package ru.otus.otuskotlin.calendar.api.v1

import ru.otus.otuskotlin.calendar.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = EventCreateRequest(
        debug = EventDebug(
            mode = EventRequestDebugMode.STUB,
            stub = EventRequestDebugStubs.BAD_TITLE
        ),
        event = EventCreateObject(
            title = "event title",
            description = "event description",
            visibility = EventVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"title\":\\s*\"event title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as EventCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"event": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, EventCreateRequest::class.java)

        assertEquals(null, obj.event)
    }
}
