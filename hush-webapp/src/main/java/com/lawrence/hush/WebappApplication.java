package com.lawrence.hush;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class WebappApplication {

    /**
     * 设置转换器和RestTemplate
     *
     * @return RestTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new FormHttpMessageConverter());

        // 设置httpMessageConverter
        FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter4 = new FastJsonHttpMessageConverter4();

        // 设置fastJsonConfig
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        fastJsonHttpMessageConverter4.setFastJsonConfig(fastJsonConfig);

        // 设置mediaTypes
        ArrayList<MediaType> mediaTypes = new ArrayList<>(Arrays.asList(MediaType.APPLICATION_JSON_UTF8,
                MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XHTML_XML, MediaType.TEXT_HTML,
                MediaType.TEXT_PLAIN, MediaType.TEXT_XML));
        fastJsonHttpMessageConverter4.setSupportedMediaTypes(mediaTypes);

        messageConverters.add(fastJsonHttpMessageConverter4);

        return new RestTemplate(messageConverters);
    }

	public static void main(String[] args) {
		SpringApplication.run(WebappApplication.class, args);
	}
}
