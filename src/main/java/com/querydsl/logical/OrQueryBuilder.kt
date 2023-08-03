package com.querydsl.logical

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.querydsl.ParseException
import com.querydsl.SearchModule

class OrQueryBuilder : CompositeCondition() {

    companion object {
        fun parseContent(fieldName: String, parser: JsonParser): OrQueryBuilder {
            var token = parser.currentToken
            if (token != JsonToken.START_ARRAY && parser.nextToken() != JsonToken.START_ARRAY) {
                throw ParseException("Expected [" + JsonToken.START_ARRAY + "] but found [" + token + "]");
            }
            var orQueryBuilder = OrQueryBuilder()
            while (parser.nextToken().also { token = it } != JsonToken.END_ARRAY) {
                token = parser.currentToken;
                if (token != JsonToken.START_OBJECT && parser.nextToken() != JsonToken.START_OBJECT) {
                    throw ParseException("Expected [" + JsonToken.START_OBJECT + "] but found [" + token + "]");
                }
                while (parser.nextToken().also { token = it } != JsonToken.END_OBJECT) {
                    if (token == JsonToken.FIELD_NAME) {
                        var queryBuilder = SearchModule.lookup(parser.currentName).parse(parser.currentName, parser)
                        orQueryBuilder.queryBuilders.add(queryBuilder)
                    }
                }
            }
            return orQueryBuilder
        }
    }

    override fun toPrepareStatementSql(params: MutableMap<Int, Any>): String {
        return "("+ queryBuilders.map { it.toPrepareStatementSql(params) }.reduce{ a, b -> "$a or $b" } + ")"
    }

    override fun toString(): String {
        return "("+ queryBuilders.map { it.toString() }.reduce{ a, b -> "$a or $b" } + ")"
    }
}