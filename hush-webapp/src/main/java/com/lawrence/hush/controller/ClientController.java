package com.lawrence.hush.controller;

import com.lawrence.hush.restapi.ServiceClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ClientController {

    @Resource
    private ServiceClient serviceClient;

    @RequestMapping("/client")
    public String client() {

        System.out.println("hush-webapp请求hush-admin服务: /service");

        return serviceClient.service("lawrence");
    }

}
