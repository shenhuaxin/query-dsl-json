package com.querydsl.comparison

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.querydsl.ParseException
import com.querydsl.ValueQueryBuilder
import com.querydsl.spring.DbConfig

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

    override fun toPrepareStatementSql(params: MutableMap<Int, Any>): String {
        var id = params.size + 1
        params[id] = value
        return "${DbConfig.FIELD_SAFE_UNQUOTE}$field${DbConfig.FIELD_SAFE_UNQUOTE} = #{${id}}"
    }

    override fun toString(): String {
        if (value is String) {
            return "${field} = '${value}'"
        }
        return "$field = $value"
    }
}