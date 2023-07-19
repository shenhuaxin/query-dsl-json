package com.querydsl

import com.querydsl.logical.AndQueryBuilder
import com.querydsl.logical.OrQueryBuilder
import com.querydsl.comparison.EqValueQueryBuilder
import com.querydsl.comparison.InValueQueryBuilder
import com.querydsl.comparison.NeValueQueryBuilder
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
            valueSpecs.put("\$eq", QuerySpec("\$eq", EqValueQueryBuilder::parseContent))
            valueSpecs.put("\$ne", QuerySpec("\$ne", NeValueQueryBuilder::parseContent))
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