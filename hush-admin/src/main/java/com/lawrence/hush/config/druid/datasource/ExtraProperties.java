package com.lawrence.hush.config.druid.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * extra数据源配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "hush-admin.datasource.extra")
public class ExtraProperties implements DataSourceProperties {

    public static final String DATASOURCE_TYPE = "extra";

    private String url;
    private String username;
    private String password;
    private String driverClassName = "com.mysql.jdbc.Driver";
    private Integer initialSize = 5;
    private Integer minIdle = 2;
    private Integer maxActive = 20;
    private Integer maxWait = 60000;
    private Integer timeBetweenEvictionRunsMillis = 60000;
    private String validationQuery = "select 1";
    private Integer minEvictableIdleTimeMillis = 300000;
    private Boolean testWhileIdle = true;
    private Boolean testOnBorrow = false;
    private Boolean testOnReturn = false;
    private Boolean poolPreparedStatements = true;
    private Integer maxPoolPreparedStatementPerConnectionSize = 20;
    private String filters = "stat,wall,slf4j";
    private String connectionProperties = "druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000";

}
