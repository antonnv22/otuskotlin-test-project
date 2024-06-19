package ru.otus.otuskotlin.calendar.mappers.v1.exceptions

class UnknownRequestClass(clazz: Class<*>) : RuntimeException("Class $clazz cannot be mapped to CalendarContext")
