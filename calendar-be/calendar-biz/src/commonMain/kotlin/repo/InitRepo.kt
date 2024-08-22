package ru.otus.otuskotlin.calendar.biz.repo

import ru.otus.otuskotlin.calendar.biz.exceptions.CalendarEventDbNotConfiguredException
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.helpers.errorSystem
import ru.otus.otuskotlin.calendar.common.helpers.fail
import ru.otus.otuskotlin.calendar.common.models.CalendarWorkMode
import ru.otus.otuskotlin.calendar.common.repo.IRepoEvent
import ru.otus.otuskotlin.calendar.cor.ICorChainDsl
import ru.otus.otuskotlin.calendar.cor.worker

fun ICorChainDsl<CalendarContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        eventRepo = when {
            workMode == CalendarWorkMode.TEST -> corSettings.repoTest
            workMode == CalendarWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != CalendarWorkMode.STUB && eventRepo == IRepoEvent.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = CalendarEventDbNotConfiguredException(workMode)
            )
        )
    }
}
