package com.querydsl.spring;

import com.querydsl.mybatis.DynamicSqlMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamicSqlConfiguration implements InitializingBean {


    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        sqlSessionFactory.getConfiguration().getMapperRegistry().addMapper(DynamicSqlMapper.class);
    }
}
