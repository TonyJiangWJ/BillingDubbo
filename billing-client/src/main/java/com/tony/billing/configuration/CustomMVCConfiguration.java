package com.tony.billing.configuration;

import com.tony.billing.interceptors.AuthorityInterceptor;
import com.tony.billing.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author by TonyJiang on 2017/6/3.
 */
@Configuration
public class CustomMVCConfiguration implements WebMvcConfigurer {

    @Autowired
    private AuthorityInterceptor authorityInterceptor;
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorityInterceptor)
                .excludePathPatterns("/bootDemo/user/login*")
                .excludePathPatterns("/bootDemo/user/register/put*")
                .excludePathPatterns("/bootDemo/user/pre/reset/password")
                .excludePathPatterns("/bootDemo/user/reset/password")
                .addPathPatterns("/bootDemo/**");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/bootDemo/user/login*")
                .addPathPatterns("/bootDemo/user/register/put*");
    }
}
