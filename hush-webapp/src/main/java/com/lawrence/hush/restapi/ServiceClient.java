package com.lawrence.hush.restapi;

import com.alibaba.fastjson.JSONObject;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "hush-admin", fallbackFactory = ServiceClientFallbackFactory.class)
public interface ServiceClient {

    @RequestMapping(value = "/test/service", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    String service(@RequestParam("type") String type, @RequestHeader("Cookie") String cookie, @RequestBody JSONObject json);

    @RequestMapping(value = "/test/config", method = RequestMethod.GET)
    String config();

}

/**
 * ServiceClient服务接口的降级处理实现
 */
@Component
class ServiceClientFallbackFactory implements FallbackFactory<ServiceClient> {

    @Override
    public ServiceClient create(Throwable cause) {

        return new ServiceClient() {

            @Override
            public String service(String type, String cookie, JSONObject json) {

                return "调用hush-admin服务: /test/service失败, " + cause.getMessage();
            }

            @Override
            public String config() {

                return "调用hush-admin服务: /test/config, " + cause.getMessage();
            }
        };
    }
}


