package com.querydsl;

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.querydsl.logical.AndQueryBuilder


class QueryBuilders {


    val queryBuilders: MutableList<IQueryBuilder> = mutableListOf();

    constructor(jsonParser: JsonParser){
        parse(jsonParser);
    }

    fun parse(parser: JsonParser) {
        var token = parser.currentToken
        if (token != JsonToken.START_OBJECT && parser.nextToken() != JsonToken.START_OBJECT) {
            throw ParseException("Expected [" + JsonToken.START_OBJECT + "] but found [" + token + "]");
        }
        var currentFieldName: String? = null
        while (parser.nextToken().also { token = it } != JsonToken.END_OBJECT) {
            if (token == JsonToken.FIELD_NAME) {
                queryBuilders.add(SearchModule.lookup(parser.currentName).parse(parser.currentName, parser))
            }
        }
    }

    override fun toString(): String {
        var andQueryBuilder = AndQueryBuilder()
        andQueryBuilder.queryBuilders.addAll(queryBuilders)
        return andQueryBuilder.toString()
    }

    fun toSql(params: MutableMap<Int, Any>): String {
        if (queryBuilders.size > 1) {
            var andQueryBuilder = AndQueryBuilder()
            andQueryBuilder.queryBuilders.addAll(queryBuilders)
            return andQueryBuilder.toSql(params)
        }
        return queryBuilders[0].toSql(params);
    }

}
