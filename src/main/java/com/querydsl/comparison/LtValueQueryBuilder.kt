package com.querydsl.comparison

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.querydsl.ParseException
import com.querydsl.ValueQueryBuilder
import com.querydsl.spring.DbConfig

class LtValueQueryBuilder (field:String, value:Any): ValueQueryBuilder() {
    val field: String
    val value: Any
    init {
        this.field = field
        this.value = value
    }


    companion object {
        val NAME = "\$lt"
        fun parseContent(fieldName: String,parser: JsonParser): LtValueQueryBuilder {
            var token = parser.currentToken
            if (!token.isScalarValue) {
                throw ParseException("$NAME 操作符必须使用字面值")
            }
            if (token == JsonToken.VALUE_STRING) {
                return LtValueQueryBuilder(fieldName, parser.valueAsString)
            } else if (token == JsonToken.VALUE_NUMBER_INT) {
                return LtValueQueryBuilder(fieldName, parser.valueAsInt)
            } else if (token == JsonToken.VALUE_NUMBER_FLOAT) {
                return LtValueQueryBuilder(fieldName, parser.valueAsDouble)
            }
            throw ParseException("$NAME 操作符只支持string、number")
        }
    }

    override fun toPrepareStatementSql(params: MutableMap<Int, Any>): String {
        var id = params.size + 1
        params[id] = value
        return "${getSafeField(field)} < #{${id}}"
    }

    override fun toString(): String {
        if (value is String) {
            return "${getSafeField(field)} < '${value}'"
        }
        return "${getSafeField(field)} < $value"
    }
}