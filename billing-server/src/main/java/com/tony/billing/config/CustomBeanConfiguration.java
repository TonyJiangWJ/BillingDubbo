package com.tony.billing.config;

import com.tony.billing.util.RSAUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangwenjie 2019-08-15
 */
@Configuration
public class CustomBeanConfiguration {

    @Value("${rsa.key.path}")
    private String rsaKeyPath;

    @Bean
    public RSAUtil rsaUtil() throws Exception {
        return new RSAUtil(rsaKeyPath);
    }

}
