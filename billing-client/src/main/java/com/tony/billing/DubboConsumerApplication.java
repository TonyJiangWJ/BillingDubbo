package com.tony.billing;

import com.tony.billing.listeners.LoggingListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author jiangwenjie 2019-08-13
 */
@ServletComponentScan
@SpringBootApplication(scanBasePackages = {"com.tony.billing"})
public class DubboConsumerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DubboConsumerApplication.class)
                .listeners(new LoggingListener())
                .run(args);
    }

}
