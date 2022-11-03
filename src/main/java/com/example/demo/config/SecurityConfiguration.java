package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
 * The SecurityConfiguration class defines 
 * the configuration for Spring Security.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    /*
     * Public paths doesn't require a user
     * to be logged in.
     */
    private static final String[] PUBLIC_PATHS = new String[] {
        "/", 
        "/wishes/**",
        "/wishlist-shares/**", 
        "/wish-reservers/**",
        "/users/new",
        "/users/create",
        "/css/**"
    };

    /*
     * Authorized paths require the user
     * to login.
     */
    private static final String[] AUTHORIZED_PATHS = new String[] {
        "/wishlists**",
        "/wishes**",
        "/users**",
        "/edit-wishlist-shares",
        "/edit-wish-reservers"
    };

    /*
     * Defines the password encoder.
     */
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /*
     * The required role of 
     * the authenticated user.
     */
    public static final String AUTHORIZED_ROLE = "USER";

    /*
     * The url the user is redirected 
     * to after a successful login. 
     */
    private static final String SUCCESS_LOGIN_URL = "/";

    /*
     * The url the user is redirected 
     * to after a successful logout. 
     */
    private static final String SUCCESS_LOGOUT_URL = "/";

    /*
     * Defines the service returning 
     * the authenticated user details.
     */
    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

    /*
     * Configure Security filter
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()

            /* Public path configuration */
            .antMatchers(PUBLIC_PATHS).permitAll()
            
            /* Authorized path configuration */
            .and()
                .authorizeRequests()
                .antMatchers(AUTHORIZED_PATHS)
                .hasRole(AUTHORIZED_ROLE)
                .anyRequest()
                .authenticated()
            
            /* HTML Login page configuration */
            .and()
                .formLogin()
                .defaultSuccessUrl(SUCCESS_LOGIN_URL, true)

            /* HTML Logout page configuration */
            .and()
                .logout()
                .logoutSuccessUrl(SUCCESS_LOGOUT_URL)

            .and()
            .httpBasic();
            
        return http.build();
    }

    /*
     * Configure the password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() { 
        return PASSWORD_ENCODER; 
    }    
}