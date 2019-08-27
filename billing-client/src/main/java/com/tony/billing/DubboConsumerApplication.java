package com.tony.billing;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author jiangwenjie 2019-08-13
 */
@ServletComponentScan
@SpringBootApplication
public class DubboConsumerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DubboConsumerApplication.class).run(args);
    }

}
