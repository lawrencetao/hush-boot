package com.lawrence.hush;

import com.lawrence.hush.config.DynamicFilterConfig;
import com.lawrence.hush.config.HushErrorAttributes;
import com.lawrence.hush.filter.GateTimeFilter;
import com.netflix.zuul.FilterFileManager;
import com.netflix.zuul.FilterLoader;
import com.netflix.zuul.groovy.GroovyCompiler;
import com.netflix.zuul.groovy.GroovyFileFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * zuul网关, 注册到eureka提供服务, 调用config服务
 */
@EnableZuulProxy
@EnableConfigurationProperties(DynamicFilterConfig.class)
@SpringCloudApplication
public class GatewayApplication {

    /**
     * 自定义错误属性
     *
     * @return DefaultErrorAttributes
     */
    @Bean
    public DefaultErrorAttributes attributes() {

        return new HushErrorAttributes();
    }

    /**
     * 自定义过滤器
     */
    @Bean
    public GateTimeFilter gateHostFilter() {

        return new GateTimeFilter();
    }

    /**
     * 动态过滤器加载器
     */
    @Bean
    public FilterLoader filterLoader(DynamicFilterConfig filterConfig) {
        FilterLoader filterLoader = FilterLoader.getInstance();
        filterLoader.setCompiler(new GroovyCompiler());

        try {
            FilterFileManager.setFilenameFilter(new GroovyFileFilter());

            // 设置动态filter路径
            String root = filterConfig.getRoot().replaceAll("\\s*", "");;
            String[] directories =  root.split(",");
            FilterFileManager.init(filterConfig.getInterval(), directories);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return filterLoader;
    }

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
}
