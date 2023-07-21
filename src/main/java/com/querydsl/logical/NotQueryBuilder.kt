package com.querydsl.logical

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.querydsl.IQueryBuilder
import com.querydsl.ParseException
import com.querydsl.SearchModule

class NotQueryBuilder: IQueryBuilder {

    var queryBuilder: IQueryBuilder? = null;

    companion object {
        fun parseContent(fieldName: String, parser: JsonParser): NotQueryBuilder {
            var token = parser.currentToken
            if (token != JsonToken.START_OBJECT && parser.nextToken() != JsonToken.START_OBJECT) {
                throw ParseException("Expected [" + JsonToken.START_OBJECT + "] but found [" + token + "]");
            }
            var notQueryBuilder = NotQueryBuilder()
            while (parser.nextToken().also { token = it } != JsonToken.END_OBJECT) {
                notQueryBuilder.queryBuilder = SearchModule.lookup(parser.currentName).parse(parser.currentName, parser)
            }
            return notQueryBuilder
        }
    }

    override fun toPrepareStatementSql(params: MutableMap<Int, Any>): String {
        return "not (${queryBuilder?.toPrepareStatementSql(params)})"
    }

    override fun toString(): String {
        return "not (${queryBuilder.toString()})"
    }
}