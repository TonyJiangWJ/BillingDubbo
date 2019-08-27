package com.tony.billing.configuration;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @author jiangwenjie 2019-03-20
 */
@Configuration
public class ValidatorConfig {

    @Value("${validation.fail.fast:true}")
    private String failFast;

    @Bean
    public Validator validator() {
        return Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(Boolean.valueOf(failFast))
                .buildValidatorFactory()
                .getValidator();
    }
}
