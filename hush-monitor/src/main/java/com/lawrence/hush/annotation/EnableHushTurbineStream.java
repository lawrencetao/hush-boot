package com.lawrence.hush.annotation;

import com.lawrence.hush.config.HushTurbineStreamConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自定义注解, 引入自定义turbine-stream配置
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({HushTurbineStreamConfig.class})
public @interface EnableHushTurbineStream {

}
