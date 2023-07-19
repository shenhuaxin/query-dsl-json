package com.querydsl

import com.fasterxml.jackson.core.JsonParser

@FunctionalInterface
fun interface QueryBuilderParser<out T: IQueryBuilder> {

    fun parse(fieldName:String, parser: JsonParser):T
}