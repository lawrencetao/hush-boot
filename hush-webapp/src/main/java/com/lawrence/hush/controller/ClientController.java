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

    /**
     * feign实现client调用服务
     *
     * @param type
     * @return String
     */
    @RequestMapping("/feignClient")
    public String client(String type) {
        JSONObject json = new JSONObject();
        json.put("test", "json");
        String cookie = "SESSION=" + UUID.randomUUID();

        return serviceClient.service(StringUtil.isNull(type) ? "-1" : type, cookie, json);
    }

    /**
     * ribbon通过restTemplate实现client调用服务
     *
     * @param type
     * @return String
     */
    @RequestMapping("/restClient")
    public String test(String type) {
        JSONObject json = new JSONObject();
        json.put("test", "json");
        String cookie = "SESSION=" + UUID.randomUUID();

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookie);

        Map<String, String> uriVeriables = new HashMap<>();
        uriVeriables.put("type", StringUtil.isNull(type) ? "-1" : type);

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restHttpUtil.restPost("http://hush-admin/service?type={type}",
                    uriVeriables, headers, json, "application/json; charset=utf-8");

            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();

            return "调用hush-admin服务失败: " + e.getMessage();
        }
    }

}
