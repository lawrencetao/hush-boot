package com.lawrence.hush.controller;

import com.alibaba.fastjson.JSONObject;
import com.lawrence.hush.restapi.ServiceClient;
import com.lawrence.hush.util.RestHttpUtil;
import com.lawrence.hush.util.StringUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class ClientController {

    @Resource
    private ServiceClient serviceClient;
    @Resource
    private RestHttpUtil restHttpUtil;

    @RequestMapping("/feignClient")
    public String client(String type) {
        if (StringUtil.isNull(type)) {
            type = "1";
        }

        System.out.println("hush-webapp请求hush-admin服务: /service");

        JSONObject json = new JSONObject();
        json.put("test", "json");
        String cookie = "SESSION=" + UUID.randomUUID();

        return serviceClient.service(type, cookie, json);
    }

    @RequestMapping("/restClient")
    public String test() {

        System.out.println("hush-webapp请求hush-admin服务: /service");

        JSONObject json = new JSONObject();
        json.put("test", "json");
        String cookie = "SESSION=" + UUID.randomUUID();

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookie);

        ResponseEntity<String> responseEntity = restHttpUtil.restPost("http://hush-admin/service?name=lawrence",
                null, headers, json, "application/json; charset=utf-8");

        return responseEntity.getBody();
    }

}
