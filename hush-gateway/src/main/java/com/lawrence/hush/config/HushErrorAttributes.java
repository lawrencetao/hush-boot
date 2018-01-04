package com.lawrence.hush.config;

import com.netflix.zuul.exception.ZuulException;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;

public class HushErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {

        // 获取父类返回map
        Map<String, Object> result = super.getErrorAttributes(requestAttributes, includeStackTrace);

        // result内容
        /*{
            "timestamp": 1514889811725,
            "status": "500",
            "error": "Internal Server Error",
            "exception": "com.netflix.zuul.exception.ZuulException",
            "message": "pre:AccessTokenFilter"
        }*/

        // TODO 自定义result处理

        return result;
    }
}