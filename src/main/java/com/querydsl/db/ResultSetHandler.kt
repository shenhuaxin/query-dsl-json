package com.querydsl.db

import cn.hutool.core.convert.Convert
import cn.hutool.core.util.StrUtil
import java.lang.reflect.Type
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

class ResultSetHandler {

    fun toList(resultSet: ResultSet): List<Map<String, Any?>> {
        var metaData = resultSet.metaData
        var columnCount = metaData.columnCount

        // 保存原始字段名
        var columnLabelList = mutableListOf<String>("")
        for (i in 1..columnCount) {
            var columnLabel = metaData.getColumnLabel(i)
            columnLabelList.add(columnLabel)
        }
        var result = mutableListOf<Map<String, Any?>>()
        while (resultSet.next()) {
            var map = mutableMapOf<String, Any?>()
            var type: Int
            for (i in 1..columnCount) {
                type = metaData.getColumnType(i)
                if ("rownum_".equals(columnLabelList[i], ignoreCase = true)) {
                    // issue#2618@Github
                    // 分页时会查出rownum字段，此处忽略掉读取
                    continue
                }
                map.put(columnLabelList[i], getColumnValue(resultSet, i, type, null))
            }
            result.add(map)
        }
        return result
    }

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