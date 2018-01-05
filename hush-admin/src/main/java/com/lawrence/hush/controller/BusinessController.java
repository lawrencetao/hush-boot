package com.lawrence.hush.controller;

import com.alibaba.fastjson.JSONObject;
import com.lawrence.hush.base.BaseController;
import com.lawrence.hush.config.druid.datasource.MultiProperties;
import com.lawrence.hush.config.druid.datasource.SingleProperties;
import com.lawrence.hush.model.Dependencies;
import com.lawrence.hush.model.Spans;
import com.lawrence.hush.service.DependenciesService;
import com.lawrence.hush.service.SpansService;
import com.lawrence.hush.util.StringUtil;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RefreshScope
@RestController
@RequestMapping("/busi")
public class BusinessController extends BaseController {

    @Resource
    private SpansService spansService;
    @Resource
    private DependenciesService dependenciesService;

    /**
     * 验证多数据源add
     *
     * @param request, json
     * @return JSONObject
     */
    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject add(HttpServletRequest request) {
        String type = request.getParameter("type");

        if (!SingleProperties.ENUM_TYPE.equals(type) && !MultiProperties.ENUM_TYPE.equals(type)) {
            type = SingleProperties.ENUM_TYPE;
        }

        Dependencies dependencies = new Dependencies();
        dependencies.setParent(StringUtil.getUUIDStr());
        dependencies.setCallCount(new Random().nextLong());
        dependencies.setChild(StringUtil.getUUIDStr());
        dependencies.setDay(new Date());
        dependencies.setErrorCount(new Random().nextLong());

        switch (type) {
            case SingleProperties.ENUM_TYPE :
                dependenciesService.addSingleDependencies(dependencies);
                break;
            case MultiProperties.ENUM_TYPE :
                dependenciesService.addMultiDependencies(dependencies);
                break;
        }

        return pubResponseJson("200", "添加dependencies成功", dependencies);
    }

    /**
     * 验证多数据源query
     *
     * @param request
     * @return JSONObject
     */
    @RequestMapping(value = "/query", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject query(HttpServletRequest request) {
        String type = request.getParameter("type");

        if (!SingleProperties.ENUM_TYPE.equals(type) && !MultiProperties.ENUM_TYPE.equals(type)) {
            type = SingleProperties.ENUM_TYPE;
        }

        List<Spans> spansList = null;

        switch (type) {
            case SingleProperties.ENUM_TYPE :
                spansList = spansService.querySingleByName("http:/test/config");
                break;
            case MultiProperties.ENUM_TYPE :
                spansList = spansService.queryMultiByName("http:/test/config");
                break;
        }

        return pubResponseJson("200", "查询spans成功", spansList);
    }

    /**
     * 验证多数据源query和add
     *
     * @param request
     * @return JSONObject
     */
    @RequestMapping(value = "/oper", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject oper(HttpServletRequest request) {
        String type = request.getParameter("type");

        if (!SingleProperties.ENUM_TYPE.equals(type) && !MultiProperties.ENUM_TYPE.equals(type)) {
            type = SingleProperties.ENUM_TYPE;
        }

        Dependencies dependencies = new Dependencies();
        dependencies.setParent(StringUtil.getUUIDStr());
        dependencies.setCallCount(new Random().nextLong());
        dependencies.setChild(StringUtil.getUUIDStr());
        dependencies.setDay(new Date());
        dependencies.setErrorCount(new Random().nextLong());
        List<Spans> spansList = null;

        switch (type) {
            case SingleProperties.ENUM_TYPE :
                dependenciesService.addSingleDependencies(dependencies);
                spansList = spansService.queryMultiByName("http:/test/config");
                break;
            case MultiProperties.ENUM_TYPE :
                dependenciesService.addMultiDependencies(dependencies);
                spansList = spansService.querySingleByName("http:/test/config");
                break;
        }

        return pubResponseJson("200", "操作成功", spansList);
    }

}
