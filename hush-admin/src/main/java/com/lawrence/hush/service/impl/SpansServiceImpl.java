package com.lawrence.hush.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lawrence.hush.annotation.ServiceDataSource;
import com.lawrence.hush.config.druid.datasource.MultiProperties;
import com.lawrence.hush.dao.SpansDao;
import com.lawrence.hush.model.Spans;
import com.lawrence.hush.service.SpansService;
import com.lawrence.hush.util.LogUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SpansServiceImpl implements SpansService {

    @Resource
    private SpansDao spansDao;

    /**
     * Single数据源, 根据name查询spans
     *
     * @param name
     * @return List<Spans>
     */
    @ServiceDataSource
    @Override
    public List<Spans> querySingleByName(String name) {
        Page<Spans> page = PageHelper.startPage(5, 10);
        spansDao.query(name);

        LogUtil.info(getClass(), "分页参数: " + page.toString());

        return page.getResult();
    }

    /**
     * Multi数据源, 根据name查询spans
     *
     * @param name
     * @return List<Spans>
     */
    @ServiceDataSource(type = MultiProperties.ENUM_TYPE)
    @Override
    public List<Spans> queryMultiByName(String name) {
        Page<Spans> page = PageHelper.startPage(5, 10);
        spansDao.query(name);

        LogUtil.info(getClass(), "分页参数: " + page.toString());

        return page.getResult();
    }

}
