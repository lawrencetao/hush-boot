package com.lawrence.hush.filter.pre;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;

/**
 * groovy脚本过滤器
 */
class AccessTokenFilter extends ZuulFilter {

    /**
     * 过滤器类型, pre(路由之前)
     *
     * @return String
     */
    @Override
    String filterType() {

        return "pre";
    }

    /**
     * 过滤器顺序
     *
     * @return int
     */
    @Override
    int filterOrder() {

        return 0;
    }

    /**
     * 是否过滤
     *
     * @return boolean
     */
    @Override
    boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        // 若uri包含/config则进行过滤
        if (request.getRequestURI().contains("/config")) {

            return true;
        }

        return false;
    }

    /**
     * 过滤逻辑
     *
     * @return Object
     */
    @SuppressWarnings("ChangeToOperator")
    @Override
    Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        // 校验accessToken
        String accessToken = request.getParameter("accessToken");

        if (!"lawrence".equals(accessToken)) {
            JSONObject resJson = new JSONObject();
            resJson.put("code", "401");
            resJson.put("message", "权限异常, 请进行认证");

            // 过滤当前请求, 并返回设定的response
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
            requestContext.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            requestContext.setResponseBody(resJson.toString());
        }

        return null;
    }

}
