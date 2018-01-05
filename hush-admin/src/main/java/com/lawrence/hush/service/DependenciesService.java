package com.lawrence.hush.service;

import com.lawrence.hush.model.Dependencies;

public interface DependenciesService {

    void addSingleDependencies(Dependencies dependencies);
    void addMultiDependencies(Dependencies dependencies);

}
