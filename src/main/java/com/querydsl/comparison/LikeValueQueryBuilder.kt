package com.querydsl.comparison

import cn.hutool.core.lang.UUID
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.querydsl.ParseException
import com.querydsl.ValueQueryBuilder

class LikeValueQueryBuilder(field:String, value:Any): ValueQueryBuilder() {


    val field: String
    val value: Any
    init {
        this.field = field
        this.value = value
    }

    companion object {
        val NAME = "\$like"
        fun parseContent(fieldName: String,parser: JsonParser): LikeValueQueryBuilder {
            var token = parser.currentToken
            if (!token.isScalarValue) {
                throw ParseException("$NAME 操作符必须使用字面值")
            }
            if (token == JsonToken.VALUE_STRING) {
                return LikeValueQueryBuilder(fieldName, parser.valueAsString)
            } else if (token == JsonToken.VALUE_NUMBER_INT) {
                return LikeValueQueryBuilder(fieldName, parser.valueAsInt)
            } else if (token == JsonToken.VALUE_NUMBER_FLOAT) {
                return LikeValueQueryBuilder(fieldName, parser.valueAsDouble)
            }
            throw ParseException("$NAME 操作符只支持string、number")
        }
    }

    override fun toSql(params: MutableMap<Int, Any>): String {
        var id = params.size + 1
        params[id] = value
        return "$field like \${${id}}"
    }

    override fun toString(): String {
        return "${field} like '${value}'"
    }
}