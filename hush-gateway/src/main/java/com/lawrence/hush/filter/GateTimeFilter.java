package com.lawrence.hush.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class GateTimeFilter extends ZuulFilter {

    /**
     * 过滤器类型, route(路由处理中)
     *
     * @return String
     */
    @Override
    public String filterType() {

        return "route";
    }

    /**
     * 过滤器顺序
     *
     * @return int
     */
    @Override
    public int filterOrder() {

        return 505;
    }

    /**
     * 是否过滤
     *
     * @return boolean
     */
    @Override
    public boolean shouldFilter() {

        return true;
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

        log.info("请求地址参数: " + request.getRequestURI() + "?" + request.getQueryString());

        return null;
    }



}
