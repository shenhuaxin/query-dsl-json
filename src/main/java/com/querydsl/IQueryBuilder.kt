package com.querydsl

interface IQueryBuilder {
    fun toSql(params: MutableMap<Int, Any>): String
}