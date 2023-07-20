package com.querydsl.comparison

import cn.hutool.core.lang.UUID
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.querydsl.ParseException
import com.querydsl.ValueQueryBuilder

class EqValueQueryBuilder(field:String, value:Any): ValueQueryBuilder() {


    val field: String
    val value: Any
    init {
        this.field = field
        this.value = value
    }

    companion object {
        val NAME = "\$eq"
        fun parseContent(fieldName: String,parser: JsonParser): EqValueQueryBuilder {
            var token = parser.currentToken
            if (!token.isScalarValue) {
                throw ParseException("$NAME 操作符必须使用字面值")
            }
            if (token == JsonToken.VALUE_STRING) {
                return EqValueQueryBuilder(fieldName, parser.valueAsString)
            } else if (token == JsonToken.VALUE_NUMBER_INT) {
                return EqValueQueryBuilder(fieldName, parser.valueAsInt)
            } else if (token == JsonToken.VALUE_NUMBER_FLOAT) {
                return EqValueQueryBuilder(fieldName, parser.valueAsDouble)
            }
            throw ParseException("$NAME 操作符只支持string、number")
        }
    }

    override fun toSql(params: MutableMap<Int, Any>): String {
        var id = params.size + 1
        params[id] = value
        return "$field = \${${id}}"
    }

    override fun toString(): String {
        if (value is String) {
            return "${field} = '${value}'"
        }
        return "$field = $value"
    }
}