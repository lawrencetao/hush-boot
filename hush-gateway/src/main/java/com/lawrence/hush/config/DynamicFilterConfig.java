package com.lawrence.hush.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 动态加载过滤器配置
 */
@Data
@ConfigurationProperties(prefix = "zuul.dynamic-filter")
public class DynamicFilterConfig {

    private String root;
    private Integer interval;

}
