package com.lawrence.hush.config.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.lawrence.hush.config.AdminProperties;
import com.lawrence.hush.config.druid.DruidConfig;
import com.lawrence.hush.config.druid.datasource.DataSourceType;
import com.lawrence.hush.config.druid.datasource.DynamicDataSource;
import com.lawrence.hush.config.druid.datasource.MultiProperties;
import com.lawrence.hush.config.druid.datasource.SingleProperties;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement(order = 2)// 让spring事务在数据源切换aop之后加载
@MapperScan(basePackages = "com.lawrence.hush.dao")
public class MybatisConfig {

    @Resource
    private AdminProperties adminProperties;
    @Resource
    private DruidConfig druidConfig;

    /**
     * 加载动态切换数据源
     *
     * @return AbstractRoutingDataSource
     */
    @Bean
    public AbstractRoutingDataSource dataSource(SingleProperties singleProperties, MultiProperties multiProperties) {

        // single数据源
        DruidDataSource singleDataSource = new DruidDataSource();
        druidConfig.initDatasource(singleDataSource, singleProperties);

        // 数据源map
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.single.getType(), singleDataSource);

        // 是否开启multi数据源
        boolean multi = adminProperties.getMultiDatasourceOpen();
        if (multi) {

            // multi数据源
            DruidDataSource multiDataSource = new DruidDataSource();
            druidConfig.initDatasource(multiDataSource, multiProperties);

            targetDataSources.put(DataSourceType.multi.getType(), multiDataSource);
        }

        // 数据源key和url对应的map
        Map<String, String> keyUrlMap = new HashMap<>();
        keyUrlMap.put(DataSourceType.single.getType(), singleProperties.getUrl());
        keyUrlMap.put(DataSourceType.multi.getType(), multiProperties.getUrl());

        DynamicDataSource dynamicDataSource = new DynamicDataSource(keyUrlMap);

        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(singleDataSource);

        // 数据源map, 包含所有数据源
        dynamicDataSource.setTargetDataSources(targetDataSources);

        return dynamicDataSource;
    }

    /**
     * SqlSessionTemplate配置
     *
     * @return SqlSessionTemplate
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {

        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 事务管理
     *
     * @return DataSourceTransactionManager
     */
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(AbstractRoutingDataSource dataSource) {

        return new DataSourceTransactionManager(dataSource);
    }

}
