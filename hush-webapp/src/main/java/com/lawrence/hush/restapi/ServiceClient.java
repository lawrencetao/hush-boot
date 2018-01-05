package com.lawrence.hush.restapi;

import com.alibaba.fastjson.JSONObject;
import com.lawrence.hush.util.LogUtil;
import feign.hystrix.FallbackFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * feign声明式调用接口
 */
@FeignClient(name = "hush-admin", fallbackFactory = ServiceClientFallbackFactory.class)
public interface ServiceClient {

    @RequestMapping(value = "/test/service", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    String service(@RequestParam("type") String type, @RequestHeader("Cookie") String cookie, @RequestBody JSONObject json);

    @RequestMapping(value = "/test/config", method = RequestMethod.GET)
    String config();

}

/**
 * ServiceClient服务接口降级处理实现
 */
@Component
class ServiceClientFallbackFactory implements FallbackFactory<ServiceClient> {

    @Override
    public ServiceClient create(Throwable cause) {

        return new ServiceClient() {

            @Override
            public String service(String type, String cookie, JSONObject json) {
                try {
                    logStackTrace(cause);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return "调用hush-admin服务: /test/service失败";
            }

            @Override
            public String config() {
                try {
                    logStackTrace(cause);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return "调用hush-admin服务: /test/config失败";
            }

            /* 打印throwable信息 */
            private void logStackTrace(Throwable cause) throws Exception {
                while (cause.getCause() != null) {
                    if (!StringUtils.isBlank(cause.getMessage())) {
                        break;
                    }
                    cause = cause.getCause();
                }

                LogUtil.info(getClass(), "降级异常: " + cause.getMessage() +
                        "\n" + Arrays.toString(cause.getStackTrace()).replace(",", "\n"));

            }

        };
    }
}


