package com.lawrence.hush.controller;

import com.alibaba.fastjson.JSONObject;
import com.lawrence.hush.base.BaseController;
import com.lawrence.hush.model.Dependencies;
import com.lawrence.hush.model.Spans;
import com.lawrence.hush.service.DependenciesService;
import com.lawrence.hush.service.SpansService;
import com.lawrence.hush.util.StringUtil;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 业务controller
 */
@RefreshScope
@RestController
@RequestMapping("/busi")
public class BusinessController extends BaseController {

    @Resource
    private SpansService spansService;
    @Resource
    private DependenciesService dependenciesService;

    /**
     * 验证多数据源query和add
     *
     * @param request
     * @return JSONObject
     */
    @RequestMapping(value = "/oper", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject oper(HttpServletRequest request) {
        String type = request.getParameter("type");

        if (!"default".equals(type) && !"extra".equals(type)) {
            type = "default";
        }

        Dependencies dependencies = new Dependencies();
        dependencies.setParent(StringUtil.getUUIDStr());
        dependencies.setCallCount(new Random().nextLong());
        dependencies.setChild(StringUtil.getUUIDStr());
        dependencies.setDay(new Date());
        dependencies.setErrorCount(new Random().nextLong());
        List<Spans> spansList = null;

        switch (type) {
            case "default" :
                dependenciesService.addDependencies(dependencies);
                spansList = spansService.queryExtraByName("http:/test/config");
                break;
            case "extra" :
                dependenciesService.addExtraDependencies(dependencies);
                spansList = spansService.queryByName("http:/test/config");
                break;
        }

        return pubResponseJson("200", "操作成功", spansList);
    }

    /**
     * 验证session共享
     *
     * @param request
     * @return JSONObject
     */
    @RequestMapping(value = "/session")
    @ApiIgnore
    public JSONObject session(HttpServletRequest request) {
        HttpSession session = request.getSession();

        String uid = (String) session.getAttribute("uid");
        if (StringUtil.isNull(uid)) {
            uid = StringUtil.getUUIDStr();
        }

        session.setAttribute("uid", uid);

        JSONObject json = new JSONObject();
        json.put("uid", uid);

        return json;
    }

}
