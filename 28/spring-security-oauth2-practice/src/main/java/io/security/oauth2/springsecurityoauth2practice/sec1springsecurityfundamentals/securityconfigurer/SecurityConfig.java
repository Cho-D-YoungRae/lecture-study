package io.security.oauth2.springsecurityoauth2practice.sec1springsecurityfundamentals.securityconfigurer;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(config -> config
                        .anyRequest().authenticated()
                )
                .apply(new CustomSecurityConfigurer()
                        .setFlag(false))
        ;
        return http.build();
    }
}
