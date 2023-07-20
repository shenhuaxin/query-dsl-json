package com.querydsl.db

import org.apache.ibatis.jdbc.SQL
import java.sql.Connection
import java.sql.ResultSet
import java.util.regex.Matcher
import java.util.regex.Pattern

class QueryExecutor() {

    fun query(connection: Connection, sql: String, params: Map<Int, Any>): ResultSet? {
        val pattern = Pattern.compile("\\$\\{([^}]+)\\}")
        val matcher: Matcher = pattern.matcher(sql)

        val matches = mutableListOf<String>()
        while (matcher.find()) {
            val match = matcher.group(1)
            matches.add(match)
        }
        var prepareSql = matcher.replaceAll("?")
        val statement = connection.prepareStatement(prepareSql)
        SQL().SELECT()
        matches.map { it.toInt() }.forEach {
            statement.setObject(it, params[it])
        }
        return statement.executeQuery()
    }
}