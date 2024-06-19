package ru.otus.otuskotlin.calendar.api.v2

import kotlinx.serialization.encodeToString
import ru.otus.otuskotlin.calendar.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV2SerializationTest {
    private val response: IResponse = EventCreateResponse(
        event = EventResponseObject(
            title = "event title",
            description = "event description",
            visibility = EventVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
//        val json = apiV2Mapper.encodeToString(EventRequestSerializer1, request)
//        val json = apiV2Mapper.encodeToString(RequestSerializers.create, request)
        val json = apiV2Mapper.encodeToString(response)

        println(json)

        assertContains(json, Regex("\"title\":\\s*\"event title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(response)
        val obj = apiV2Mapper.decodeFromString<IResponse>(json) as EventCreateResponse

        assertEquals(response, obj)
    }
}
