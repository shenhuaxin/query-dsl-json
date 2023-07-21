package com.querydsl

import com.querydsl.db.QueryExecutor
import com.querydsl.db.ResultSetHandler
import com.querydsl.mybatis.DynamicSqlMapper
import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import java.io.File
import java.sql.Statement
import java.util.regex.Matcher
import java.util.regex.Pattern


object App {
    @JvmStatic
    fun main(args: Array<String>) {
        val resource = "mybatis-config.xml"
        try {
            val `in` = Resources.getResourceAsStream(resource)
            val ssfb = SqlSessionFactoryBuilder()
            val ssf = ssfb.build(`in`)
            val sqlSession = ssf.openSession()

            var params = mutableMapOf<String, Any>();

            val content = File("/Users/shx/code/github/query-dsl-json/src/main/resources/query.json").readText()
            val parse = SearchBuilder().parse(content)
            var map = mutableMapOf<Int, Any>()
            var toSql = parse.toSql(map)

            var mapper = sqlSession.getMapper(DynamicSqlMapper::class.java)
            params.put("sql", toSql)
            for (entry in map) {
                params.put(entry.key.toString(), entry.value)
            }
            var result = mapper.listBySql(params)

//            val connection = sqlSession.connection
//

//            var resultSet = QueryExecutor().query(connection, toSql, map)
//
//            var result = resultSet?.let { ResultSetHandler().toList(it) }
            println(result)
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }
}
