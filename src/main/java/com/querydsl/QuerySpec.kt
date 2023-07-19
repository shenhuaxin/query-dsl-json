package com.querydsl


data class QuerySpec<in T : IQueryBuilder>(
    val name: String?,
    val parser: QueryBuilderParser<IQueryBuilder>,
)
