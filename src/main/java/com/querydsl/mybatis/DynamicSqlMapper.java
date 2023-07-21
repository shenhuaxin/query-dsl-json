package com.querydsl.mybatis;

import java.util.List;
import java.util.Map;

public interface DynamicSqlMapper {

    List<Map<String, Object>> listBySql(Map<String, Object> paramterMap);
}
