package com.querydsl

import com.querydsl.comparison.EqValueQueryBuilder
import java.io.File

fun main() {
    var content = File("/Users/shx/code/github/query-dsl-json/src/main/resources/query.json").readText()
    var parse = SearchBuilder().parse(content)
    println(parse.toString())

}