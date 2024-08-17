package ru.otus.otuskotlin.calendar.biz

import ru.otus.otuskotlin.calendar.biz.general.initStatus
import ru.otus.otuskotlin.calendar.biz.general.operation
import ru.otus.otuskotlin.calendar.biz.general.stubs
import ru.otus.otuskotlin.calendar.biz.repo.*
import ru.otus.otuskotlin.calendar.biz.stubs.*
import ru.otus.otuskotlin.calendar.biz.validation.*
import ru.otus.otuskotlin.calendar.common.CalendarContext
import ru.otus.otuskotlin.calendar.common.CalendarCorSettings
import ru.otus.otuskotlin.calendar.common.models.CalendarCommand
import ru.otus.otuskotlin.calendar.common.models.CalendarEventId
import ru.otus.otuskotlin.calendar.common.models.CalendarEventLock
import ru.otus.otuskotlin.calendar.common.models.CalendarState
import ru.otus.otuskotlin.calendar.cor.chain
import ru.otus.otuskotlin.calendar.cor.rootChain
import ru.otus.otuskotlin.calendar.cor.worker

class CalendarEventProcessor(
    private val corSettings: CalendarCorSettings = CalendarCorSettings.NONE
) {
    suspend fun exec(ctx: CalendarContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<CalendarContext> {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")

        operation("Создание события", CalendarCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadStart("Имитация ошибки валидации начала события")
                stubValidationBadEnd("Имитация ошибки валидации завершения события")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в eventValidating") { eventValidating = eventRequest.deepCopy() }
                worker("Очистка id") { eventValidating.id = CalendarEventId.NONE }
                worker("Очистка заголовка") { eventValidating.title = eventValidating.title.trim() }
                worker("Очистка описания") { eventValidating.description = eventValidating.description.trim() }
                validateTitleNotEmpty("Проверка, что заголовок не пуст")
                validateTitleHasContent("Проверка символов")
                validateDescriptionNotEmpty("Проверка, что описание не пусто")
                validateDescriptionHasContent("Проверка символов")
                validateStartNotEmpty("Проверка, что дата начала события не пустое")
                validateEndNotEmpty("Проверка, что дата завершения события не пустое")
                finishEventValidation("Завершение проверок")
            }
            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание события в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Получить событие", CalendarCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadStart("Имитация ошибки валидации начала события")
                stubValidationBadEnd("Имитация ошибки валидации завершения события")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в eventValidating") { eventValidating = eventRequest.deepCopy() }
                worker("Очистка id") { eventValidating.id = CalendarEventId(eventValidating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishEventValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика чтения"
                repoRead("Чтение события из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == CalendarState.RUNNING }
                    handle { eventRepoDone = eventRepoRead }
                }
            }
            prepareResult("Подготовка ответа")
        }
        operation("Изменить событие", CalendarCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadStart("Имитация ошибки валидации начала события")
                stubValidationBadEnd("Имитация ошибки валидации завершения события")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в eventValidating") { eventValidating = eventRequest.deepCopy() }
                worker("Очистка id") { eventValidating.id = CalendarEventId(eventValidating.id.asString().trim()) }
                worker("Очистка lock") { eventValidating.lock = CalendarEventLock(eventValidating.lock.asString().trim()) }
                worker("Очистка заголовка") { eventValidating.title = eventValidating.title.trim() }
                worker("Очистка описания") { eventValidating.description = eventValidating.description.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                validateTitleNotEmpty("Проверка на непустой заголовок")
                validateTitleHasContent("Проверка на наличие содержания в заголовке")
                validateDescriptionNotEmpty("Проверка на непустое описание")
                validateDescriptionHasContent("Проверка на наличие содержания в описании")

                finishEventValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика сохранения"
                repoRead("Чтение события из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareUpdate("Подготовка объекта для обновления")
                repoUpdate("Обновление события в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Удалить событие", CalendarCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в eventValidating") {
                    eventValidating = eventRequest.deepCopy()
                }
                worker("Очистка id") { eventValidating.id = CalendarEventId(eventValidating.id.asString().trim()) }
                worker("Очистка lock") { eventValidating.lock = CalendarEventLock(eventValidating.lock.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                finishEventValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика удаления"
                repoRead("Чтение события из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление события из БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Поиск событий", CalendarCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в eventFilterValidating") { eventFilterValidating = eventFilterRequest.deepCopy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishEventFilterValidation("Успешное завершение процедуры валидации")
            }
            repoSearch("Поиск события в БД по фильтру")
            prepareResult("Подготовка ответа")
        }
    }.build()
}

