package com.querydsl.spring;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "querydsl.db")
public class DbConfig {

    public static final String FIELD_SAFE_UNQUOTE = "\"";
}
