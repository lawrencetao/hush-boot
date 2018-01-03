package com.lawrence.hush.config;

import com.netflix.zuul.exception.ZuulException;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;

public class HushErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(
            RequestAttributes requestAttributes, boolean includeStackTrace) {

        requestAttributes.getSessionId();

        // 获取父类返回map
        Map<String, Object> result = super.getErrorAttributes(requestAttributes, includeStackTrace);

        /** TODO 自定义返回状态及结果
        {
            "timestamp": 1514889811725,
            "status": "500",
            "error": "Internal Server Error",
            "exception": "com.netflix.zuul.exception.ZuulException",
            "message": "pre:AccessTokenFilter"
        }*/

        // 获取返回异常
        Object obj = requestAttributes.getAttribute("javax.servlet.error.exception", 0);
        if (obj instanceof ZuulException) {
            ZuulException zuulException = (ZuulException) obj;
            String causeMsg = zuulException.getCause().getMessage();

            // 正则, 匹配三位数字 + 井号 + n位非#字符
            String regex = "[0-9]{3,}#([^#\\w]|[^#\\W])+";

            if (causeMsg.matches(regex)) {
                result.put("code", causeMsg.split("#")[0]);
                result.put("description", causeMsg.split("#")[1]);
            }
        }

        return result;
    }
}