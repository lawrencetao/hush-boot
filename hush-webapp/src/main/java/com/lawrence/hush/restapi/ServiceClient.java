package com.lawrence.hush.restapi;

import com.alibaba.fastjson.JSONObject;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "hush-admin", fallbackFactory = ServiceClientFallbackFactory.class)
public interface ServiceClient {

    @RequestMapping(value = "/service", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    String service(@RequestParam("type") String type, @RequestHeader("Cookie") String cookie, @RequestBody JSONObject json);

}

/**
 * ServiceClient服务接口的降级处理实现
 */
@Component
class ServiceClientFallbackFactory implements FallbackFactory<ServiceClient> {

    @Override
    public ServiceClient create(Throwable cause) {

        return (type, cookie, json) -> "调用hush-admin服务失败: " + cause.getMessage();
    }
}


