package com.tony.billing.listeners;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author jiangwenjie 2020/9/9
 */
@Slf4j
public class LoggingListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {
    private final String LOG_PATH = "log4j2.path";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEvent) {
        ConfigurableEnvironment environment = applicationEvent.getEnvironment();
        String logPath = environment.getProperty(LOG_PATH);
        if (StringUtils.isNotEmpty(logPath)) {
            log.info("设置日志地址：{}", logPath);
            System.setProperty(LOG_PATH, logPath);
        }
    }

    /**
     * 设置顺序在日志初始化监听器之前
     *
     * @return
     */
    @Override
    public int getOrder() {
        return LoggingApplicationListener.DEFAULT_ORDER - 1;
    }
}
