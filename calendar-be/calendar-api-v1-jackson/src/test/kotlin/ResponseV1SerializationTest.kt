package ru.otus.otuskotlin.calendar.api.v1

import ru.otus.otuskotlin.calendar.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = EventCreateResponse(
        event = EventResponseObject(
            title = "event title",
            description = "event description",
            start = "2024-08-23T19:00:00Z",
            end = "2024-08-23T19:30:00Z",
            visibility = EventVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
        assertContains(json, Regex("\"title\":\\s*\"event title\""))
        assertContains(json, Regex("\"description\":\\s*\"event description\""))
        assertContains(json, Regex("\"start\":\\s*\"2024-08-23T19:00:00Z\""))
        assertContains(json, Regex("\"end\":\\s*\"2024-08-23T19:30:00Z\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as EventCreateResponse

        assertEquals(response, obj)
    }
}
