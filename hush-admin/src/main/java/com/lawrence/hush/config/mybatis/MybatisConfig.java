package com.lawrence.hush.config.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.lawrence.hush.config.AdminProperties;
import com.lawrence.hush.config.druid.DruidConfig;
import com.lawrence.hush.config.druid.datasource.DataSourceType;
import com.lawrence.hush.config.druid.datasource.DynamicDataSource;
import com.lawrence.hush.config.druid.datasource.ExtraProperties;
import com.lawrence.hush.config.druid.datasource.DefaultProperties;
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

/**
 * mybatis配置
 */
@Configuration
@EnableTransactionManagement(order = 2)// spring事务在数据源切换aop之后加载
@MapperScan(basePackages = "com.lawrence.hush.dao")
public class MybatisConfig {

    @Resource
    private AdminProperties adminProperties;
    @Resource
    private DruidConfig druidConfig;

    @Resource
    private DefaultProperties defaultProperties;
    @Resource
    private ExtraProperties extraProperties;

    /**
     * 加载动态切换数据源
     *
     * @return AbstractRoutingDataSource
     */
    @Bean
    public AbstractRoutingDataSource abstractRoutingDataSource() {

        // 配置默认数据源
        DruidDataSource defaultDataSource = new DruidDataSource();
        druidConfig.initDatasource(defaultDataSource, defaultProperties);

        // 数据源map, 添加默认数据源key/dataSource
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.DEFAULT.getType(), defaultDataSource);

        // 是否开启extra数据源
        boolean multiOpen = adminProperties.getMultiDataSourceOpen();
        if (multiOpen) {

            // 配置extra数据源
            DruidDataSource extraDataSource = new DruidDataSource();
            druidConfig.initDatasource(extraDataSource, extraProperties);

            targetDataSources.put(DataSourceType.EXTRA.getType(), extraDataSource);
        }

        // 数据源key和url对应的map
        Map<String, String> keyUrlMap = new HashMap<>();
        keyUrlMap.put(DataSourceType.DEFAULT.getType(), defaultProperties.getUrl());
        keyUrlMap.put(DataSourceType.EXTRA.getType(), extraProperties.getUrl());

        DynamicDataSource dynamicDataSource = new DynamicDataSource(keyUrlMap);

        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);

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
     * 事务管理配置
     *
     * @return DataSourceTransactionManager
     */
    @Bean
    public DataSourceTransactionManager transactionManager(AbstractRoutingDataSource abstractRoutingDataSource) {

        return new DataSourceTransactionManager(abstractRoutingDataSource);
    }

}
