package com.lawrence.hush.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import lombok.Getter;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.*;

/**
 * restful请求工具类
 */
@Component
public class RestHttpUtil {

    @Resource
    private RestTemplate restTemplate;

    /**
     * 发送post请求
     *
     * @param uri, param, contentType
     * @return String
     */
    public String restPost(String uri, Object param, String contentType) {
        ResponseEntity<String> responseEntity = restPost(uri, null, null, param, contentType);

        return responseEntity == null ? "" : responseEntity.getBody();
    }

    /**
     * 发送post请求, uri参数可变
     *
     * @param uri, uriVeriables, param, contentType
     * @return String
     */
    public String restPost(String uri, Map<String, String> uriVeriables, Object param, String contentType) {
        ResponseEntity<String> responseEntity = restPost(uri, uriVeriables, null, param, contentType);

        return responseEntity == null ? "" : responseEntity.getBody();
    }

    /**
     * 发送post请求, uri参数可变, 带请求头
     *
     * @param uri, veriable(uri参数中{}变量替换成对应key的value), headers, param, contentType
     * @return ResponseEntity<String>
     */
    @SuppressWarnings("unchecked")
    public ResponseEntity<String> restPost(String uri, Map<String, String> uriVeriables, Map<String, String> headers, Object param, String contentType) {

        // 校验uri非空
        checkURI(uri);

        // 设置requestHeaders
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", StringUtil.isNull(contentType) ? "application/x-www-form-urlencoded" : contentType);
        if (headers != null && headers.size() > 0) {
            Set<Map.Entry<String, String>> set = headers.entrySet();
            for (Map.Entry<String, String> entry : set) {
                httpHeaders.add(entry.getKey(), entry.getValue());
            }
        }

        // 设置requestEntity
        HttpEntity httpEntity = null;
        if (param instanceof String) {
            httpEntity = new HttpEntity<String>((String) param, httpHeaders);
        } else if (param instanceof LinkedMultiValueMap) {
            httpEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>((LinkedMultiValueMap) param, httpHeaders);
        } else if (param instanceof JSONObject) {
            httpEntity = new HttpEntity<JSONObject>((JSONObject) param, httpHeaders);
        } else if (param != null) {
            throw new RuntimeException("Param参数必须为String, JSONObject或LinkedMultiValueMap类型");
        }

        ResponseEntity<String> responseEntity;

        // uriVeriables不为空时, 替换uri中的参数
        if (uriVeriables != null && uriVeriables.size() > 0) {
            responseEntity = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class, uriVeriables);
        } else {
            responseEntity = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        }

        return responseEntity;
    }

    /**
     * 发送get请求
     *
     * @param uri
     * @return String
     */
    public String restGet(String uri) {
        ResponseEntity<String> responseEntity = restGet(uri, null, null);

        return responseEntity == null ? "" : responseEntity.getBody();
    }

    /**
     * 发送get请求, uri参数可变
     *
     * @param uri, uriVeriables
     * @return String
     */
    public String restGet(String uri, Map<String, String> uriVeriables) {
        ResponseEntity<String> responseEntity = restGet(uri, uriVeriables, null);

        return responseEntity == null ? "" : responseEntity.getBody();
    }

    /**
     * 发送get请求, uri参数可变, 带请求头
     *
     * @param uri, uriVeriables, headers
     * @return String
     */
    public ResponseEntity<String> restGet(String uri, Map<String, String> uriVeriables, Map<String, String> headers) {

        // 校验uri非空
        checkURI(uri);

        // 设置requestHeaders
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null && headers.size() > 0) {
            Set<Map.Entry<String, String>> set = headers.entrySet();
            for (Map.Entry<String, String> entry : set) {
                httpHeaders.add(entry.getKey(), entry.getValue());
            }
        }

        // 设置requestEntity
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> responseEntity;

        // uriVeriables不为空时, 替换uri中的参数
        if (uriVeriables != null && uriVeriables.size() > 0) {
            responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class, uriVeriables);
        } else {
            responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
        }

        return responseEntity;
    }

    /* 校验请求uri */
    private void checkURI(String uri) {
        if (StringUtil.isNull(uri)) {
            throw new RuntimeException("Rest请求uri不能为空");
        }
    }

}
