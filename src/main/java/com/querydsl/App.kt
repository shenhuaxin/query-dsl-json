package com.querydsl

import cn.hutool.core.convert.Convert
import cn.hutool.core.util.StrUtil
import cn.hutool.db.handler.HandleHelper
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import java.io.File
import java.lang.reflect.Type
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.sql.Types


object App {
    @JvmStatic
    fun main(args: Array<String>) {
        val resource = "mybatis-config.xml"
        try {
            val `in` = Resources.getResourceAsStream(resource)
            val ssfb = SqlSessionFactoryBuilder()
            val ssf = ssfb.build(`in`)
            val sqlSession = ssf.openSession()
            val connection = sqlSession.connection

            val content = File("/Users/shx/code/github/query-dsl-json/src/main/resources/query.json").readText()
            val parse = SearchBuilder().parse(content)
//            var selectList = sqlSession.selectList<JSONObject>(parse.toString())
            val statement: Statement = connection.createStatement()
            var resultSet = statement.executeQuery(parse.toString())


            var metaData = resultSet.metaData
            var columnCount = metaData.columnCount
            var list = mutableListOf<JSONObject>()
            while (resultSet.next()) {
            var jsonObject = JSONObject()
                var type: Int
                var columnLabel: String
                for (i in 1..columnCount) {
                    type = metaData.getColumnType(i)
                    columnLabel = metaData.getColumnLabel(i)
                    if ("rownum_".equals(columnLabel, ignoreCase = true)) {
                        // issue#2618@Github
                        // 分页时会查出rownum字段，此处忽略掉读取
                        continue
                    }
                    jsonObject.put(columnLabel, getColumnValue(resultSet, i, type, null))
                }
                list.add(jsonObject)
            }
            list.forEach { println(JSON.toJSONString(it)) }
            sqlSession.commit()
            sqlSession.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // -------------------------------------------------------------------------------------------------------------- Private method start
    @Throws(SQLException::class)
    private fun getColumnValue(rs: ResultSet, columnIndex: Int, type: Int, targetColumnType: Type?): Any? {
        var rawValue: Any? = null
        when (type) {
            Types.TIMESTAMP -> try {
                rawValue = rs.getTimestamp(columnIndex)
            } catch (ignore: SQLException) {
                // issue#776@Github
                // 当数据库中日期为0000-00-00 00:00:00报错，转为null
            }

            Types.TIME -> rawValue = rs.getTime(columnIndex)
            Types.CLOB -> rawValue = rs.getString(columnIndex)
            else -> rawValue = rs.getObject(columnIndex)
        }
        return if (null == targetColumnType || Any::class.java == targetColumnType) {
            // 无需转换
            rawValue
        } else {
            // 按照返回值要求转换
            Convert.convert(targetColumnType, rawValue)
        }
    }
}
