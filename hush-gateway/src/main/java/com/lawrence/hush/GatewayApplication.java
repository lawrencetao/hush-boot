package com.lawrence.hush;

import com.lawrence.hush.filter.AccessTokenFilter;
import com.lawrence.hush.config.HushErrorAttributes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

    /**
     * gateway过滤器
     *
     * @return AccessTokenFilter
     */
    @Bean
    public AccessTokenFilter accessFilter() {

        return new AccessTokenFilter();
    }

    /**
     * 自定义错误属性
     *
     * @return DefaultErrorAttributes
     */
    @Bean
    public DefaultErrorAttributes attributes() {

        return new HushErrorAttributes();
    }

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
}
