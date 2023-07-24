package com.querydsl.comparison

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.querydsl.ParseException
import com.querydsl.ValueQueryBuilder
import com.querydsl.spring.DbConfig

class GtValueQueryBuilder(field:String, value:Any): ValueQueryBuilder() {
    val field: String
    val value: Any
    init {
        this.field = field
        this.value = value
    }

    companion object {
        val NAME = "\$gt"
        fun parseContent(fieldName: String,parser: JsonParser): GtValueQueryBuilder {
            var token = parser.currentToken
            if (!token.isScalarValue) {
                throw ParseException("$NAME 操作符必须使用字面值")
            }
            if (token == JsonToken.VALUE_STRING) {
                return GtValueQueryBuilder(fieldName, parser.valueAsString)
            } else if (token == JsonToken.VALUE_NUMBER_INT) {
                return GtValueQueryBuilder(fieldName, parser.valueAsInt)
            } else if (token == JsonToken.VALUE_NUMBER_FLOAT) {
                return GtValueQueryBuilder(fieldName, parser.valueAsDouble)
            }
            throw ParseException("$NAME 操作符只支持string、number")
        }
    }

    override fun toPrepareStatementSql(params: MutableMap<Int, Any>): String {
        var id = params.size + 1
        params[id] = value
        return "${DbConfig.getFieldSafeDelimiter()}$field${DbConfig.getFieldSafeDelimiter()} > #{${id}}"
    }

    override fun toString(): String {
        if (value is String) {
            return "${field} > '${value}'"
        }
        return "$field > $value"
    }
}