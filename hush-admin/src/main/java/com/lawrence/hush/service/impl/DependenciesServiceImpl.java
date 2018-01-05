package com.lawrence.hush.service.impl;

import com.lawrence.hush.annotation.ServiceDataSource;
import com.lawrence.hush.config.druid.datasource.MultiProperties;
import com.lawrence.hush.config.druid.datasource.SingleProperties;
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
     * Single数据源, 添加dependencies
     *
     * @param dependencies
     */
    @ServiceDataSource(type = SingleProperties.ENUM_TYPE)
    @Override
    public void addSingleDependencies(Dependencies dependencies) {
        dependenciesDao.insertSelective(dependencies);
    }

    /**
     * Multi数据源, 添加dependencies
     *
     * @param dependencies
     */
    @ServiceDataSource(type = MultiProperties.ENUM_TYPE)
    @Override
    public void addMultiDependencies(Dependencies dependencies) {
        dependenciesDao.insertSelective(dependencies);
    }

}
