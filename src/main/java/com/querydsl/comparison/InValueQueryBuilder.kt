package com.querydsl.comparison

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.querydsl.ParseException
import com.querydsl.ValueQueryBuilder
import kotlin.text.StringBuilder

class InValueQueryBuilder(fieldName: String, values: List<Any>) : ValueQueryBuilder() {
    val field: String;
    val values: List<Any>
    init {
        this.field = fieldName
        this.values = values
    }

    companion object {
        fun parseContent(fieldName: String,parser: JsonParser): InValueQueryBuilder {
            var token = parser.currentToken;
            if (token != JsonToken.START_ARRAY) {
                throw ParseException("\$in 操作符必须使用数组")
            }
            var args = mutableListOf<Any>()
            while (parser.nextToken().also { token=it } != JsonToken.END_ARRAY) {
                if (token == JsonToken.VALUE_STRING) {
                    args.add(parser.valueAsString)
                } else if (token == JsonToken.VALUE_NUMBER_INT) {
                    args.add(parser.valueAsLong)
                } else if (token == JsonToken.VALUE_NUMBER_FLOAT) {
                    args.add(parser.valueAsDouble)
                }
            }
            return InValueQueryBuilder(fieldName, args);
        }
    }

    override fun toString(): String {
        return "${field} in (" + values.reduce { a, b ->
            var sb = StringBuilder()
            return@reduce sb.append(if (a is String) "'${a}'" else a)
                .append(",")
                .append(if (b is String) "'${b}'" else b)
                .toString()
        } + ")"
    }
}