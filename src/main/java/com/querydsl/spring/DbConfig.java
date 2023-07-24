package com.querydsl.spring;


import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "querydsl.db")
public class DbConfig implements InitializingBean {

    private static String FIELD_SAFE_DELIMITER = "\"";

//    @Value("${querydsl.db.fieldDelimiter}")
    public String fieldDelimiter;

    @Override
    public void afterPropertiesSet() throws Exception {
        FIELD_SAFE_DELIMITER = fieldDelimiter;
    }

    public static String getFieldSafeDelimiter() {
        return FIELD_SAFE_DELIMITER;
    }

    public void setFieldDelimiter(String fieldDelimiter) {
        if (StrUtil.isNotBlank(fieldDelimiter)) {
            this.fieldDelimiter = fieldDelimiter;
        }
    }

    public String getFieldDelimiter() {
        return fieldDelimiter;
    }
}
