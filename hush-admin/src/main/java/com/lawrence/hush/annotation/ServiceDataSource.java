package com.lawrence.hush.annotation;

import com.lawrence.hush.config.druid.datasource.SingleProperties;

import java.lang.annotation.*;

/**
 * 数据源切换注解, 默认数据源: single
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ServiceDataSource {
    String type() default SingleProperties.ENUM_TYPE;
}

