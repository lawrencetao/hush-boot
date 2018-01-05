package com.lawrence.hush.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * hush-admin开关配置
 */
@Data
@Configuration
public class AdminProperties {

    @Value("${hush-admin.switch.swagger2-open}")
    private Boolean swagger2Open;
    @Value("${hush-admin.switch.multi-datasource-open}")
    private Boolean multiDatasourceOpen;

}
