package com.lawrence.hush;

import com.lawrence.hush.filter.NameTraceSampler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.sleuth.sampler.SamplerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * webapp服务, 注册到eureka提供服务, 调用config服务和admin服务
 */
@EnableFeignClients
@SpringCloudApplication
public class WebappApplication {

    /** restTemplate, ribbon默认超时时间 */
    private static int connectTimeout = 1000;
    private static int readTimeout = 1000;

    @Value("${ribbon.restTemplate.connectTimeout}")
    public void setConnectTimeout(int connectTimeout) {
        WebappApplication.connectTimeout = connectTimeout;
    }

    @Value("${ribbon.restTemplate.readTimeout}")
    public void setReadTimeout(int readTimeout) {
        WebappApplication.readTimeout = readTimeout;
    }

    /**
     * restTemplate配置
     *
     * @return RestTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(connectTimeout);
        simpleClientHttpRequestFactory.setReadTimeout(readTimeout);

        return new RestTemplate(simpleClientHttpRequestFactory);
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
		SpringApplication.run(WebappApplication.class, args);
	}
}
