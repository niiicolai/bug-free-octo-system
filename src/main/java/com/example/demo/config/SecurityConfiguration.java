package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/",
                "/wishes/**",
                "/wishlist-shares/**", 
                "/wishlist-shares-reserve/**",
                "/users/new",
                "/users/create")
            .permitAll()
            .and()
            .authorizeRequests()
            .antMatchers("/wishlists**",
                "/wishes**",
                "/users**")
            .hasRole("USER")
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
                .defaultSuccessUrl("/", true)
            .and()
            .logout()
                .logoutSuccessUrl("/")
            .and()
            .httpBasic();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    }
}