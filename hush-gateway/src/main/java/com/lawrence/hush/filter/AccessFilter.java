package com.lawrence.hush.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccessFilter extends ZuulFilter {

    /**
     * 过滤器类型, pre(路由之前)
     *
     * @return String
     */
    @Override
    public String filterType() {

        return "pre";
    }

    /**
     * 过滤器顺序
     *
     * @return int
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否过滤
     *
     * @return boolean
     */
    @Override
    public boolean shouldFilter() {
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
    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        // 校验accessToken
        String accessToken = request.getParameter("accessToken");

        if (!"lawrence".equals(accessToken)) {
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(401);
        }

        return null;
    }
}
