package com.querydsl

import com.querydsl.mybatis.DynamicSqlMapper
import com.querydsl.spring.DynamicSqlQuery
import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import java.io.File


object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val resource = "mybatis-config.xml"
        try {
            val `in` = Resources.getResourceAsStream(resource)
            val ssfb = SqlSessionFactoryBuilder()
            val ssf = ssfb.build(`in`)

            var params = mutableMapOf<String, Any>();

            val content = File("/Users/shx/code/github/query-dsl-json/src/test/resources/query.json").readText()
            val parse = SearchBuilder().parse(content)
            var map = mutableMapOf<Int, Any>()
            var toSql = parse.toString()

            println(toSql)

            ssf.configuration.mapperRegistry.addMapper(DynamicSqlMapper::class.java)
            val sqlSession = ssf.openSession()

            for (entry in map) {
                params.put(entry.key.toString(), entry.value)
            }

            var result = DynamicSqlQuery().query(sqlSession, toSql, params);
//            var mapper = sqlSession.getMapper(DynamicSqlMapper::class.java)
//            params.put("sql", toSql)
//            for (entry in map) {
//                params.put(entry.key.toString(), entry.value)
//            }
//            var result = mapper.listBySql(params)

//            val connection = sqlSession.connection
//

//            var resultSet = QueryExecutor().query(connection, toSql, map)
//
//            var result = resultSet?.let { ResultSetHandler().toList(it) }println(result)
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }
}
