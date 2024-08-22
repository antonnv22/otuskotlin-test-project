package ru.otus.otuskotlin.calendar.blackbox.test.action

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult

val beValidId = Matcher<String?> {
    MatcherResult(
        it != null,
        { "id should not be null" },
        { "id should be null" },
    )
}

val beValidLock = Matcher<String?> {
    MatcherResult(
        it != null,
        { "lock should not be null" },
        { "lock should be null" },
    )
}

