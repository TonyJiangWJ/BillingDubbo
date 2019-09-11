package com.tony.billing.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tony.billing.util.AuthUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangwj20966 8/6/2018
 */
@Configuration
public class CustomBeanConfiguration {

    @Value("${jwt.secret.key:springboot}")
    private String jwtSecretKey;

    @Bean
    public ObjectMapper jsonMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // null 不输出
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    @Bean
    public AuthUtil authUtil() {
        return new AuthUtil(new AuthUtil.JavaWebToken(jwtSecretKey));
    }

}
