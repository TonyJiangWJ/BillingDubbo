package com.tony.billing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author jiangwenjie 2020/1/6
 */

@MapperScan("com.tony.billing.dao.mapper")
@SpringBootApplication(scanBasePackages = "com.tony.billing")
public class TestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(TestApplication.class).run(args);
    }
}