package com.lawrence.hush.config.druid;

public interface DruidProperties {

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
