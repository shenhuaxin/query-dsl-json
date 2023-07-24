package com.querydsl

import cn.hutool.db.DbUtil
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.querydsl.logical.AndQueryBuilder
import com.querydsl.comparison.EqValueQueryBuilder
import com.querydsl.comparison.InValueQueryBuilder
import com.querydsl.spring.DbConfig

open abstract class ValueQueryBuilder :IQueryBuilder{


    fun getSafeField(fieldName: String): String {
        return DbConfig.getFieldSafeDelimiter()+fieldName+DbConfig.getFieldSafeDelimiter();
    }

    companion object {
        fun parseContent(fieldName: String, parser: JsonParser):IQueryBuilder {
            var token = parser.nextToken()
            if (token.isScalarValue) {
                return EqValueQueryBuilder.parseContent(fieldName, parser)
            } else if (token == JsonToken.START_ARRAY) {
                return InValueQueryBuilder.parseContent(fieldName, parser);
            } else if (token == JsonToken.START_OBJECT) {
                var andQueryBuilder = AndQueryBuilder()
                var condition = ""
                while (parser.nextToken().also { token = it } != JsonToken.END_OBJECT) {
                    if (token == JsonToken.FIELD_NAME) {
                        condition = parser.currentName
                    } else if (token.isScalarValue || token == JsonToken.START_ARRAY) {
                        andQueryBuilder.queryBuilders.add(SearchModule.lookupValueBuilder(condition).parse(fieldName, parser))
                    }
                }
                if (andQueryBuilder.queryBuilders.size == 1) {
                    return andQueryBuilder.queryBuilders[0]
                }
                return andQueryBuilder;
            }
            throw ParseException("query解析异常")
        }
    }
}