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
import java.util.Random;

@RestController
public class ServiceController {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private EurekaDiscoveryClient client;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 服务提供方法
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

                System.out.println("延迟时间: " + i);

                Thread.sleep(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Param: " + type);
        System.out.println("Content-Type: " + request.getHeader("Content-Type"));
        System.out.println("Cookie: " + request.getHeader("Cookie"));
        System.out.println("RequestBody: " + json);

        redisTemplate.opsForValue().set("test", "test");

        ServiceInstance instance = client.getLocalServiceInstance();
        System.out.println(instance.getHost() + ":" + instance.getPort() + " | " + instance.getServiceId());

        return "hush-admin服务: /service返回";
    }

}
