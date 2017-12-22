package com.lawrence.hush.restapi.impl;

import com.alibaba.fastjson.JSONObject;
import com.lawrence.hush.restapi.ServiceClient;
import org.springframework.stereotype.Component;

/**
 * ServiceClient接口方法降级处理
 */
@Component
public class ServiceClientFallback implements ServiceClient {

    @Override
    public String service(String name, String cookie, JSONObject json) {

        return "hush-admin服务rest请求失败";
    }

}
