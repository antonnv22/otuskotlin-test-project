package ru.otus.otuskotlin.calendar.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.otuskotlin.calendar.common.models.*
import ru.otus.otuskotlin.calendar.common.repo.*
import ru.otus.otuskotlin.calendar.common.repo.exceptions.RepoEmptyLockException
import ru.otus.otuskotlin.calendar.repo.common.IRepoEventInitializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class EventRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : EventRepoBase(), IRepoEvent, IRepoEventInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, EventEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(events: Collection<CalendarEvent>) = events.map { event ->
        val entity = EventEntity(event)
        require(entity.id != null)
        cache.put(entity.id, entity)
        event
    }

    override suspend fun createEvent(rq: DbEventRequest): IDbEventResponse = tryEventMethod {
        val key = randomUuid()
        val event = rq.event.copy(id = CalendarEventId(key), lock = CalendarEventLock(randomUuid()))
        val entity = EventEntity(event)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbEventResponseOk(event)
    }

    override suspend fun readEvent(rq: DbEventIdRequest): IDbEventResponse = tryEventMethod {
        val key = rq.id.takeIf { it != CalendarEventId.NONE }?.asString() ?: return@tryEventMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbEventResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateEvent(rq: DbEventRequest): IDbEventResponse = tryEventMethod {
        val rqEvent = rq.event
        val id = rqEvent.id.takeIf { it != CalendarEventId.NONE } ?: return@tryEventMethod errorEmptyId
        val key = id.asString()
        val oldLock = rqEvent.lock.takeIf { it != CalendarEventLock.NONE } ?: return@tryEventMethod errorEmptyLock(id)

        mutex.withLock {
            val oldEvent = cache.get(key)?.toInternal()
            when {
                oldEvent == null -> errorNotFound(id)
                oldEvent.lock == CalendarEventLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldEvent.lock != oldLock -> errorRepoConcurrency(oldEvent, oldLock)
                else -> {
                    val newEvent = rqEvent.copy(lock = CalendarEventLock(randomUuid()))
                    val entity = EventEntity(newEvent)
                    cache.put(key, entity)
                    DbEventResponseOk(newEvent)
                }
            }
        }
    }


    override suspend fun deleteEvent(rq: DbEventIdRequest): IDbEventResponse = tryEventMethod {
        val id = rq.id.takeIf { it != CalendarEventId.NONE } ?: return@tryEventMethod errorEmptyId
        val key = id.asString()
        val oldLock = rq.lock.takeIf { it != CalendarEventLock.NONE } ?: return@tryEventMethod errorEmptyLock(id)

        mutex.withLock {
            val oldEvent = cache.get(key)?.toInternal()
            when {
                oldEvent == null -> errorNotFound(id)
                oldEvent.lock == CalendarEventLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldEvent.lock != oldLock -> errorRepoConcurrency(oldEvent, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbEventResponseOk(oldEvent)
                }
            }
        }
    }

    /**
     * Поиск событий по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchEvent(rq: DbEventFilterRequest): IDbEventsResponse = tryEventsMethod {
        val result: List<CalendarEvent> = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != CalendarUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbEventsResponseOk(result)
    }
}
