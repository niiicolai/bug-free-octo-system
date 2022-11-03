package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

/*
 * The ThymeleafConfiguration class defines 
 * the configuration for Thymeleaf.
 */
public class ThymeleafConfiguration {

    /*
     * Configure Spring Security dialect for Thymeleaf.
     */
    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
}
