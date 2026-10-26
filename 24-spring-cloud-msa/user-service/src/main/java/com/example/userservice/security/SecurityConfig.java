package com.example.userservice.security;

import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationFilter authenticationFilter,
            @Value("${gateway.ip}") String gatewayIp
    ) throws Exception {
        return http
                .csrf().disable()

                .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest().hasIpAddress(gatewayIp)

                .and()
                .addFilter(authenticationFilter)
                .headers().frameOptions().disable()

                .and()
                .build();
    }

    @Bean
    public AuthenticationFilter authenticationFilter(
            AuthenticationManager authenticationManager,
            UserService userService,
            @Value("${token.expiration-time}") long tokenExpirationTime,
            @Value("${token.secret}") String tokenSecret
    ) {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(
                userService, tokenExpirationTime, tokenSecret);
        authenticationFilter.setAuthenticationManager(authenticationManager);
        return authenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
