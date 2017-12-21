package com.lawrence.hush.restapi;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hush-admin", fallback = ServiceClient.ServiceClientFallback.class)
public interface ServiceClient {

    @RequestMapping("/service")
    String service(@RequestParam("name") String name);


    /* ------------------------------ */


    /** hystrix降级处理方法 */
    @Component
    class ServiceClientFallback implements ServiceClient {

        @Override
        public String service(String name) {

            return "hush-admin服务rest请求失败";
        }
    }

}
