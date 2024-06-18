@file:Suppress("unused")

package ru.otus.otuskotlin.m1l5

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

// Реализуйте dsl для составления sql запроса, чтобы все тесты стали зелеными.
class Hw1Sql {
    private fun checkSQL(expected: String, sql: SqlSelectBuilder) {
        assertEquals(expected, sql.build())
    }

    private fun query(block: SqlSelectBuilder.() -> Unit) = SqlSelectBuilder().apply(block)

    @Test
    fun `simple select all from table`() {
        val expected = "select * from table"

        val real = query {
            from("table")
        }

        checkSQL(expected, real)
    }

    @Test
    fun `check that select can't be used without table`() {
        assertFailsWith<Exception> {
            query {
                select("col_a")
            }.build()
        }
    }

    @Test
    fun `select certain columns from table`() {
        val expected = "select col_a, col_b from table"

        val real = query {
            select("col_a", "col_b")
            from("table")
        }

        checkSQL(expected, real)
    }

    @Test
    fun `select certain columns from table 1`() {
        val expected = "select col_a, col_b from table"

        val real = query {
            select("col_a", "col_b")
            from("table")
        }

        checkSQL(expected, real)
    }

    /**
     * __eq__ is "equals" function. Must be one of char:
     *  - for strings - "="
     *  - for numbers - "="
     *  - for null - "is"
     */
    @Test
    fun `select with complex where condition with one condition`() {
        val expected = "select * from table where col_a = 'id'"

        val real = query {
            from("table")
            where { "col_a" eq "id" }
        }

        checkSQL(expected, real)
    }

    /**
     * __nonEq__ is "non equals" function. Must be one of chars:
     *  - for strings - "!="
     *  - for numbers - "!="
     *  - for null - "!is"
     */
    @Test
    fun `select with complex where condition with two conditions`() {
        val expected = "select * from table where col_a != 0"

        val real = query {
            from("table")
            where {
                "col_a" nonEq 0
            }
        }

        checkSQL(expected, real)
    }

    @Test
    fun `when 'or' conditions are specified then they are respected`() {
        val expected = "select * from table where (col_a = 4 or col_b !is null)"

        val real = query {
            from("table")
            where {
                or {
                    "col_a" eq 4
                    "col_b" nonEq null
                }
            }
        }

        checkSQL(expected, real)
    }
}

class Select {
    private val _selects: MutableList<String> = mutableListOf()

    val selects: List<String>
        get() = _selects.toList()

    fun add(value: String) {
        _selects.add(value)
    }

    override fun toString(): String {
        return "select ${_selects.joinToString(separator = ", ")}"
    }
}

class Source(var source: String = "") {

    override fun toString(): String {
        return "from $source"
    }
}

class Or {
    private val _conditions: MutableList<String> = mutableListOf()

    val conditions: List<String>
        get() = _conditions.toList()

    fun add(value: String) {
        _conditions.add(value)
    }

    infix fun String.eq(value: Any?) {
        when (value) {
            is String -> add("$this = '$value'")
            is Number -> add("$this = $value")
            else -> add("$this is $value")
        }
    }

    infix fun String.nonEq(value: Any?) {
        when (value) {
            is String -> add("$this != '$value'")
            is Number -> add("$this != $value")
            else -> add("$this !is $value")
        }
    }

    override fun toString(): String {
        _conditions.ifEmpty {
            return ""
        }
        return "(${_conditions.joinToString(separator = " or ")})"
    }
}

class Where() {
    private val _conditions: MutableList<String> = mutableListOf()
    private var _or: Or? = null

    val conditions: List<String>
        get() = _conditions.toList()

    fun add(value: String) {
        _conditions.add(value)
    }

    infix fun String.eq(value: Any?) {
        when (value) {
            is String -> add("$this = '$value'")
            is Number -> add("$this = $value")
            else -> add("$this is $value")
        }
    }

    infix fun String.nonEq(value: Any?) {
        when (value) {
            is String -> add("$this != '$value'")
            is Number -> add("$this != $value")
            else -> add("$this !is $value")
        }
    }

    fun or(block: Or.() -> Unit) {
        val or = Or().apply(block)
        _or = or
    }

    override fun toString(): String {
        if (_or != null) {
            return " where ${_or}"
        }
        _conditions.ifEmpty {
            return ""
        }
        return " where ${_conditions.joinToString(separator = " ")}"
    }
}

class SqlSelectBuilder {
    private var select: Select? = null
    private var source: Source? = null
    private var where: Where? = null

    fun from(value: String) {
        source = Source(value)
    }

    fun select(vararg values: String) {
        val _select = Select()
        values.forEach { _select.add(it) }
        select = _select
    }

    fun where(block: Where.() -> Unit) {
        val _where = Where().apply(block)
        where = _where
    }

    fun build(): String = this.toString()

    override fun toString(): String {
        if (source == null) {
            throw RuntimeException()
        }

        var result = ""
        if (select == null) {
            result += "select *"
        } else {
            result = select.toString()
        }

        val whereClause = where?.toString() ?: ""
        return "$result $source$whereClause"
    }
}


