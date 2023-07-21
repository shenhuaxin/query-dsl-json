package com.querydsl.logical

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.querydsl.ParseException
import com.querydsl.SearchModule

class AndQueryBuilder() : CompositeCondition() {


    companion object {
        fun parseContent(fieldName: String, parser: JsonParser): AndQueryBuilder {
            var token = parser.currentToken
            if (token != JsonToken.START_OBJECT && parser.nextToken() != JsonToken.START_OBJECT) {
                throw ParseException("Expected [" + JsonToken.START_OBJECT + "] but found [" + token + "]");
            }
            var andQueryBuilder = AndQueryBuilder()
            while (parser.nextToken().also { token = it } != JsonToken.END_OBJECT) {
                if (token == JsonToken.FIELD_NAME) {
                    var queryBuilder = SearchModule.lookup(parser.currentName).parse(parser.currentName, parser)
                    andQueryBuilder.queryBuilders.add(queryBuilder)
                }
            }
            return andQueryBuilder
        }
    }

    override fun toPrepareStatementSql(params: MutableMap<Int, Any>): String {
        return "("+ queryBuilders.map { it.toPrepareStatementSql(params) }.reduce{ a, b -> "$a and $b" } + ")"
    }

    override fun toString(): String {
        return "("+ queryBuilders.map { it.toString() }.reduce{ a, b -> "$a and $b" } + ")"
    }
}