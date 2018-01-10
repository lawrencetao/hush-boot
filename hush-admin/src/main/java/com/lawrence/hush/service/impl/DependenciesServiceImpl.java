package com.lawrence.hush.service.impl;

import com.lawrence.hush.annotation.ServiceDataSource;
import com.lawrence.hush.config.druid.datasource.ExtraProperties;
import com.lawrence.hush.dao.DependenciesDao;
import com.lawrence.hush.model.Dependencies;
import com.lawrence.hush.service.DependenciesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DependenciesServiceImpl implements DependenciesService {

    @Resource
    private DependenciesDao dependenciesDao;

    /**
     * 默认数据源, 添加dependencies
     *
     * @param dependencies
     */
    @ServiceDataSource
    @Override
    public void addDependencies(Dependencies dependencies) {
        dependenciesDao.insertSelective(dependencies);
    }

    /**
     * extra数据源, 添加dependencies
     *
     * @param dependencies
     */
    @ServiceDataSource(type = ExtraProperties.DATASOURCE_TYPE)
    @Override
    public void addExtraDependencies(Dependencies dependencies) {
        dependenciesDao.insertSelective(dependencies);
    }

}
