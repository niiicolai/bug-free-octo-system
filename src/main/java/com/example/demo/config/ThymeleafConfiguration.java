package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

public class ThymeleafConfiguration {
    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
}
