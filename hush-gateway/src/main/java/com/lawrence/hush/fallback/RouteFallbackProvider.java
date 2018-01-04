package com.lawrence.hush.fallback;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 网关降级处理
 */
@Slf4j
@Component
public class RouteFallbackProvider implements FallbackProvider {

    /**
     * 降级处理response, 带异常参数
     *
     * @param cause
     * @return ClientHttpResponse
     */
    @Override
    public ClientHttpResponse fallbackResponse(Throwable cause) {

        return new ClientHttpResponse() {

            @Override
            public HttpStatus getStatusCode() throws IOException {

                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {

                return 200;
            }

            @Override
            public String getStatusText() throws IOException {

                return "OK";
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                try {
                    logStackTrace(cause);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JSONObject resJson = new JSONObject();
                resJson.put("code", "500");
                resJson.put("message", "网关异常, 请稍后再试");

                return new ByteArrayInputStream(resJson.toString().getBytes("UTF-8"));
            }

            /* 打印throwable信息 */
            private void logStackTrace(Throwable cause) throws Exception {
                while (cause.getCause() != null) {
                    if (!StringUtils.isBlank(cause.getMessage())) {
                        break;
                    }
                    cause = cause.getCause();
                }

                log.info("降级异常: " + cause.getMessage() +
                        "\n" + Arrays.toString(cause.getStackTrace()).replace(",", "\n"));

            }

            @Override
            public HttpHeaders getHeaders() {

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

                return headers;
            }
        };
    }

    /**
     * 匹配降级的路由名称
     *
     * @return String
     */
    @Override
    public String getRoute() {

        return "*";
    }

    /**
     * 降级处理response
     *
     * @return ClientHttpResponse
     */
    @Override
    public ClientHttpResponse fallbackResponse() {

        return new ClientHttpResponse() {

            @Override
            public HttpStatus getStatusCode() throws IOException {

                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {

                return 200;
            }

            @Override
            public String getStatusText() throws IOException {

                return "OK";
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                JSONObject resJson = new JSONObject();
                resJson.put("code", "500");
                resJson.put("message", "网关异常, 请稍后再试");

                return new ByteArrayInputStream(resJson.toString().getBytes("UTF-8"));
            }

            @Override
            public HttpHeaders getHeaders() {

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

                return headers;
            }
        };
    }
}