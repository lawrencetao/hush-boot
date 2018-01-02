package com.lawrence.hush;

import com.lawrence.hush.filter.NameTraceSampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.sampler.SamplerProperties;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class AdminApplication {

	/**
	 * 配置自定义Sampler
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
