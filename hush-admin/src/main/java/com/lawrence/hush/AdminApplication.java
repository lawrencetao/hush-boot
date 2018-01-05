package com.lawrence.hush;

import com.lawrence.hush.config.AdminProperties;
import com.lawrence.hush.filter.NameTraceSampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.sampler.SamplerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * admin服务, 注册到eureka提供服务, 调用config服务
 */
@EnableTransactionManagement
@EnableDiscoveryClient
@SpringBootApplication
public class AdminApplication extends WebMvcConfigurerAdapter {

    @Resource
    private AdminProperties adminProperties;

    /**
     * 添加资源处理handler
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 设置static和templates目录的静态访问
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");

        // 添加swagger2目录的静态访问
        if (adminProperties.getSwagger2Open()) {
            registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        }

    }

    /**
     * 自定义sampler配置
     *
     * @return NameTraceSampler
     */
    @Bean
    public NameTraceSampler defaultSampler(SamplerProperties properties) {

        return new NameTraceSampler(properties);
    }

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
