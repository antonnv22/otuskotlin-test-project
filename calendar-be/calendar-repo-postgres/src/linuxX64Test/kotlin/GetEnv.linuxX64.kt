package ru.otus.otuskotlin.calendar.backend.repo.postgresql

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

@OptIn(ExperimentalForeignApi::class)
actual fun getEnv(name: String): String? = getenv("postgresPort")?.toKString()
