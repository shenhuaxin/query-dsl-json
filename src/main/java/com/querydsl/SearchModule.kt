package com.querydsl

import com.querydsl.comparison.*
import com.querydsl.logical.AndQueryBuilder
import com.querydsl.logical.OrQueryBuilder
import com.querydsl.logical.NotQueryBuilder

class SearchModule {

    companion object {
        var querySpecs: MutableMap<String, QuerySpec<IQueryBuilder>> = mutableMapOf()
        var valueSpecs: MutableMap<String, QuerySpec<IQueryBuilder>> = mutableMapOf()

        init {
            querySpecs.put("\$and", QuerySpec("\$and", AndQueryBuilder::parseContent))
            querySpecs.put("\$or", QuerySpec("\$or", OrQueryBuilder::parseContent))
            querySpecs.put("\$not", QuerySpec("\$not", NotQueryBuilder::parseContent))


            valueSpecs.put("\$in", QuerySpec("\$in", InValueQueryBuilder::parseContent))
            valueSpecs.put("\$nin", QuerySpec("\$nin", NinValueQueryBuilder::parseContent))
            valueSpecs.put("\$eq", QuerySpec("\$eq", EqValueQueryBuilder::parseContent))
            valueSpecs.put("\$ne", QuerySpec("\$ne", NeValueQueryBuilder::parseContent))
            valueSpecs.put("\$like", QuerySpec("\$like", LikeValueQueryBuilder::parseContent))
            valueSpecs.put("\$gte", QuerySpec("\$gte", GteValueQueryBuilder::parseContent))
            valueSpecs.put("\$gt", QuerySpec("\$gt", GtValueQueryBuilder::parseContent))
            valueSpecs.put("\$lte", QuerySpec("\$lte", LteValueQueryBuilder::parseContent))
            valueSpecs.put("\$lt", QuerySpec("\$lt", LtValueQueryBuilder::parseContent))
        }

        fun lookup(fieldName: String): QueryBuilderParser<IQueryBuilder> {
            var querySpec = querySpecs.getOrDefault(
                fieldName,
                QuerySpec("", ValueQueryBuilder::parseContent)
            )
            return querySpec.parser
        }


        fun lookupValueBuilder(fieldName: String): QueryBuilderParser<IQueryBuilder> {
            var querySpec = valueSpecs.getOrDefault(
                fieldName,
                QuerySpec("", ValueQueryBuilder::parseContent)
            )
            return querySpec.parser
        }
    }
}