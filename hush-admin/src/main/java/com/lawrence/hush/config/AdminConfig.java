package com.lawrence.hush.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AdminConfig {

    @Value("${hush-admin.swagger2-open}")
    private Boolean swagger2Open;

}
