package com.lawrence.hush.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ServiceController {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private EurekaDiscoveryClient client;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping("/service")
    public String service(HttpServletRequest request, @RequestBody JSONObject json) {
        String type = request.getParameter("type");

        System.out.println(type);
        System.out.println(request.getHeader("Content-Type"));
        System.out.println(request.getHeader("Cookie"));
        System.out.println(json);

        redisTemplate.opsForValue().set("test", "test");

        double db = Math.random();
        try {
            if ("0".equals(type)) {
                Thread.sleep(6000);
            } else {
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ServiceInstance instance = client.getLocalServiceInstance();

        System.out.println(instance.getHost() + ":" + instance.getPort() + " | " + instance.getServiceId());
        System.out.println("hush-admin服务: /servic被client请求");

        return "hush-admin服务: /service返回";
    }

}
