package com.querydsl.db

import org.apache.ibatis.jdbc.SQL
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory
import java.sql.Connection
import java.sql.ResultSet
import java.util.regex.Matcher
import java.util.regex.Pattern

class QueryExecutor {



    fun query(
        sqlSessionFactory: SqlSessionFactory,
        sql: String,
        params: Map<Int, Any>
    ): List<Map<String, Any?>> {
        var sqlSession = sqlSessionFactory.openSession()
        try {
            var resultSet = query(sqlSession, sql, params)
            return resultSet?.let { ResultSetHandler().toList(it) } ?: listOf();
        } finally {
            sqlSession.close();
        }
    }

    private fun query(connection: Connection, sql: String, params: Map<Int, Any>): ResultSet? {
        val pattern = Pattern.compile("#\\{([^}]+)\\}")
        val matcher: Matcher = pattern.matcher(sql)

        val matches = mutableListOf<String>()
        while (matcher.find()) {
            val match = matcher.group(1)
            matches.add(match)
        }
        var prepareSql = matcher.replaceAll("?")
        val statement = connection.prepareStatement(prepareSql)
        matches.map { it.toInt() }.forEach {
            statement.setObject(it, params[it])
        }
        return statement.executeQuery()
    }

    private fun query(
        sqlSession: SqlSession,
        sql: String,
        params: Map<Int, Any>
    ): ResultSet? {
        return query(sqlSession.connection, sql, params)
    }
}