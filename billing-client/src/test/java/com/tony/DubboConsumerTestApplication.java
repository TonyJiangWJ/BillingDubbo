package com.tony;

import com.tony.billing.listeners.LoggingListener;
import com.tony.billing.util.RSAUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

/**
 * @author jiangwenjie 2020-11-11
 */
@ServletComponentScan
@SpringBootApplication(scanBasePackages = {"com.tony.billing"})
public class DubboConsumerTestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DubboConsumerTestApplication.class)
                .listeners(new LoggingListener())
                .run(args);
    }


    @Value("${rsa.key.path}")
    private String rsaKeyPath;

    @Bean
    public RSAUtil rsaUtil() throws Exception {
        return new RSAUtil(rsaKeyPath);
    }
}
