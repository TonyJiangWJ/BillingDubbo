package com.tony.billing.config;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.sql.SQLUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author jiangwenjie 2018-12-17
 */

@Configuration
public class DruidConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.druid.filter.slf4j")
    @Primary
    public Slf4jLogFilter slf4jLogFilter() {
        Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
        slf4jLogFilter.setConnectionLogEnabled(false);
        slf4jLogFilter.setStatementLogEnabled(false);
        slf4jLogFilter.setResultSetLogEnabled(true);
        slf4jLogFilter.setStatementExecutableSqlLogEnable(true);
        SQLUtils.FormatOption formatOption = new SQLUtils.FormatOption(true, false);
        formatOption.setUppCase(true);
        formatOption.setDesensitize(false);
        slf4jLogFilter.setStatementSqlFormatOption(formatOption);
        return slf4jLogFilter;
    }
}
