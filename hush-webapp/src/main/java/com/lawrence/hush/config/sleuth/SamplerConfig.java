package com.lawrence.hush.config.sleuth;

import org.springframework.cloud.sleuth.sampler.SamplerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SamplerConfig {

    /**
     * 配置自定义Sampler
     *
     * @return NameTraceSampler
     */
    @Bean
    public NameTraceSampler defaultSampler(SamplerProperties properties) {

        return new NameTraceSampler(properties);
    }

}
