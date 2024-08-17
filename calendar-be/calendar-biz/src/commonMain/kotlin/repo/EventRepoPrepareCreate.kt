package ru.otus.otuskotlin.calendar.biz.repo

import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.cor.ICorChainDsl
import ru.otus.otuskotlin.calendar.cor.worker
import ru.otus.otuskotlin.calendar.stubs.CalendarEventStub

fun ICorChainDsl<CalendarContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == CalendarState.RUNNING }
    handle {
        eventRepoPrepare = eventValidated.deepCopy()
        eventRepoPrepare.ownerId = CalendarEventStub.get().ownerId
    }
}
