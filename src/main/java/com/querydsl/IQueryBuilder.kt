package com.querydsl

interface IQueryBuilder {

    fun toPrepareStatementSql(params: MutableMap<Int, Any>): String

}