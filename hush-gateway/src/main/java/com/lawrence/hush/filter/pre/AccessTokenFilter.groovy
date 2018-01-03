package com.lawrence.hush.filter.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;

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

        // 若uri包含feignClient则进行过滤
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

            // 返回异常格式统一, 业务代码#业务描述
            throw new RuntimeException("401#权限异常, accessToken错误");
        }

        return null;
    }

}
