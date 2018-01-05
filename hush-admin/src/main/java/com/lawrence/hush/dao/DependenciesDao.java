package com.lawrence.hush.dao;

import com.lawrence.hush.model.Dependencies;

public interface DependenciesDao {

    int insert(Dependencies record);
    int insertSelective(Dependencies record);

}