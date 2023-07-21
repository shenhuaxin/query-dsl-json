package com.querydsl.spring

import com.querydsl.mybatis.DynamicSqlMapper
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory

class DynamicSqlQuery {

    fun query(
        sqlSession: SqlSession,
        sql: String,
        params: Map<String, Any>
    ): MutableList<MutableMap<String, Any>>? {
            var mapper = sqlSession.getMapper(DynamicSqlMapper::class.java)

            var sqlParams = mutableMapOf<String, Any>();
            sqlParams.put("sql", sql)
            for (entry in params) {
                sqlParams.put(entry.key, entry.value)
            }
            return mapper.listBySql(sqlParams);
    }

    fun query(
        sqlSessionFactory: SqlSessionFactory,
        sql: String,
        params: Map<String, Any>
    ): MutableList<MutableMap<String, Any>>? {
        var sqlSession = sqlSessionFactory.openSession()
        try {
            return query(sqlSession, sql, params)
        } finally {
            sqlSession.close();
        }
    }
}