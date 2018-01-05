package com.lawrence.hush.aop;

import com.lawrence.hush.annotation.ServiceDataSource;
import com.lawrence.hush.config.druid.datasource.DynamicDataSource;
import com.lawrence.hush.config.druid.datasource.SingleProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.PriorityOrdered;

import java.lang.reflect.Method;

/**
 * aop切换数据源
 */
@Aspect
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@Configuration
@ConditionalOnExpression("${hush-admin.switch.multi-datasource-open}")
public class DataSourceAop implements PriorityOrdered {

    /**
     * 有注解的service方法, 判断并切换数据源
     */
    @Pointcut(value = "execution(* com.lawrence.hush.service.impl..*.*(..)) && " +
            "(@annotation(com.lawrence.hush.annotation.ServiceDataSource))")
    private void isAnnotation() {}

    @Around("isAnnotation()")
    public Object switchDataSource(ProceedingJoinPoint point) throws Throwable {

        Signature signature = point.getSignature();
        MethodSignature methodSignature;
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("读写注解只能作用于方法");
        }
        methodSignature = (MethodSignature) signature;

        Object target = point.getTarget();
        Method currentMethod = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());

        // 获取注解对象, 和属性值type
        ServiceDataSource serviceDataSource = currentMethod.getAnnotation(ServiceDataSource.class);
        DynamicDataSource.setJdbcType(serviceDataSource.type());

        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clear();
        }

    }

    /**
     * 无注解的service方法, 默认single数据源
     */
    @Pointcut(value = "execution(* com.lawrence.hush.service.impl..*.*(..)) && " +
            "(!@annotation(com.lawrence.hush.annotation.ServiceDataSource))")
    private void isNotAnnotation() {}

    @Around("isNotAnnotation()")
    public Object defaultDataSource(ProceedingJoinPoint point) throws Throwable {
        DynamicDataSource.setJdbcType(SingleProperties.ENUM_TYPE);

        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clear();
        }

    }

    /**
     * aop切换数据源加载顺序
     *
     * @return int
     */
    @Override
    public int getOrder() {

        return 1;
    }

}
