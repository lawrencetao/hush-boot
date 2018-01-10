package com.lawrence.hush.config.druid.datasource;

/**
 * 数据源配置接口
 */
public interface DataSourceProperties {

    // 数据源key
    String DATASOURCE_TYPE = "default";

    String getUrl();
    String getUsername();
    String getPassword();

    String getDriverClassName();
    Integer getInitialSize();
    Integer getMinIdle();
    Integer getMaxActive();
    Integer getMaxWait();
    Integer getTimeBetweenEvictionRunsMillis();
    Integer getMinEvictableIdleTimeMillis();
    String getValidationQuery();
    Boolean getTestWhileIdle();
    Boolean getTestOnBorrow();
    Boolean getTestOnReturn();
    Boolean getPoolPreparedStatements();
    Integer getMaxPoolPreparedStatementPerConnectionSize();
    String getFilters();

}
