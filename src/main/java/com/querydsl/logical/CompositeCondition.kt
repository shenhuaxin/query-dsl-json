package com.querydsl.logical;

import com.querydsl.IQueryBuilder

abstract class CompositeCondition:IQueryBuilder {
    val queryBuilders: MutableList<IQueryBuilder> = mutableListOf();

}
