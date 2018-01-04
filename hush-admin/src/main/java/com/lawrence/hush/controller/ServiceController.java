package com.lawrence.hush.controller;

import com.alibaba.fastjson.JSONObject;
import com.lawrence.hush.redis.RedisOperator;
import com.lawrence.hush.test.TestObj;
import com.lawrence.hush.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@RefreshScope
@RestController
@RequestMapping("/test")
public class ServiceController {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private EurekaDiscoveryClient client;
    @Autowired
    private RedisOperator redisOperator;

    /**
     * 验证feign和ribbon-restTemplate服务提供方法
     *
     * @param request, json
     * @return String
     */
    @RequestMapping("/service")
    public String service(HttpServletRequest request, @RequestBody JSONObject json) {
        String type = request.getParameter("type");

        double db = Math.random();
        try {
            if ("0".equals(type)) {// 必定超时
                Thread.sleep(6000);
            } else if ("1".equals(type)) {// 不超时
                Thread.sleep(3000);
            } else {// 随机是否超时
                long i =  new Random().nextInt(7500);

                LogUtil.info(getClass(), "延迟时间: " + i);

                Thread.sleep(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LogUtil.info(getClass(), "Param: " + type + "\n" +
                "Content-Type: " + request.getHeader("Content-Type") + "\n" +
                "Cookie: " + request.getHeader("Cookie") + "\n" +
                "RequestBody: " + json);

        ServiceInstance instance = client.getLocalServiceInstance();
        LogUtil.info(getClass(), instance.getHost() + ":" + instance.getPort() + " | " + instance.getServiceId());

        return "hush-admin服务: /service返回";
    }

    @Value("${hush-admin.dynamic.param}")
    private String param;

    /**
     * 获取可动态刷新的配置
     */
    @RequestMapping("/config")
    public JSONObject config(HttpServletRequest request) {
        TestObj to = new TestObj();
        to.setId("test");
        to.setFee(100);
        redisOperator.setObject("test", to);
        to.setNum(10);
        to = redisOperator.getObject("test");

        JSONObject json = new JSONObject();
        json.put("param", param);
        json.put("word", "中文");
        json.put("testObj", to);

        LogUtil.info(getClass(), "返回json: " + json.toString());

        return json;
    }

}
