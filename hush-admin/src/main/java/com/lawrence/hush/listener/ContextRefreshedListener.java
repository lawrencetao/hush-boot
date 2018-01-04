package com.lawrence.hush.listener;

import com.lawrence.hush.util.LogUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * spring容器监听器
 */
@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 容器加载完成后执行制定操作
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        LogUtil.info(getClass(), "ContextRefreshedEvent监听事件调用, Hush-boot: Spring容器加载完成");

    }

}
