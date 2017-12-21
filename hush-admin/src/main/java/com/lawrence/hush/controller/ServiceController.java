package com.lawrence.hush.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private EurekaDiscoveryClient client;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping("/service")
    public String service(String name) {
        redisTemplate.opsForValue().set("name", name);
        System.out.println(redisTemplate.opsForValue().get("name"));

        ServiceInstance instance = client.getLocalServiceInstance();

        System.out.println(instance.getHost() + ":" + instance.getPort() + " | " + instance.getServiceId());
        System.out.println("hush-admin服务: /servic被client请求");

        return "hush-admin服务: /service返回";
    }

}
