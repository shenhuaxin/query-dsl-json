package com.querydsl;

import cn.hutool.core.util.StrUtil
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken;

class SearchBuilder {

    val DETECT_SQL_INJECTION_REGEX = "/('(''|[^'])*')|(;)|(\b(ALTER|CREATE|DELETE|DROP|EXEC(UTE){0,1}|INSERT( +INTO){0,1}|MERGE|SELECT|UPDATE|UNION( +ALL){0,1})\b)/i"
    var select: String? = null;
    var table: String? = null;
    var queryBuilders: QueryBuilders? = null;

    val FROM_FIELD: String = "from";
    val SELECT_FIELD: String = "select";
    val QUERY_FIELD: String = "query";

    fun parse(content: String):SearchBuilder {
        val parser: JsonParser = JsonFactory().createParser(content)
        var token = parser.currentToken
        if (token != JsonToken.START_OBJECT && parser.nextToken() != JsonToken.START_OBJECT) {
            throw ParseException("Expected [" + JsonToken.START_OBJECT + "] but found [" + token + "]");
        }
        var currentFieldName: String? = null

        while ((parser.nextToken().also { token = it }) != JsonToken.END_OBJECT) {
            if (token == JsonToken.FIELD_NAME) {
                currentFieldName = parser.currentName
            } else if(token.isScalarValue) {
                if (currentFieldName?.lowercase().equals(FROM_FIELD)) {
                    from(parser.valueAsString)
                } else if (currentFieldName?.lowercase().equals(SELECT_FIELD)) {
                    select(parser.valueAsString)
                }
            } else if (token == JsonToken.START_OBJECT){
                if (currentFieldName?.lowercase().equals(QUERY_FIELD)) {
                    queryBuilders = QueryBuilders(parser)
                }
            }
        }
        return this;
    }

    private fun from(table: String) {
        this.table = StrUtil.cleanBlank(table)
    }

    private fun select(fields: String) {
        this.select = fields;
    }

    override fun toString(): String {
        return "${toSelect()} ${toFrom()} where ${queryBuilders.toString()}"
    }

    fun toSql(params: MutableMap<Int, Any>): String {
        if (queryBuilders != null) {
            return "${toSelect()} ${toFrom()} where ${queryBuilders?.toSql(params)}"
        } else {
            return "${toSelect()} ${toFrom()}"
        }
    }

    private fun toFrom(): String {
        if (table == null || table == "") {
            return "from T_ADS_DIM_APP_DATA"
        }
        return "from " + StrUtil.cleanBlank(table)
    }

    private fun toSelect(): String {
        if (StrUtil.isBlank(select)) {
            return "select *"
        }
        var selectField = select?.split(",")?.map { StrUtil.cleanBlank(it) }?.reduce { a, b -> "$a, $b" }
        return "select $selectField"
    }
}
