package com.lawrence.hush.restapi;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "hush-admin")
public interface ServiceClient {

    @RequestMapping(value = "/service", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    String service(@RequestParam("name") String name, @RequestHeader("Cookie") String cookie, @RequestBody JSONObject json);

}


