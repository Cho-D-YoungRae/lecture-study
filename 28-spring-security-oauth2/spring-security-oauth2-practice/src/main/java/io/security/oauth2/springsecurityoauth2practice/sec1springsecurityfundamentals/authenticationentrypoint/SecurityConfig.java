package io.security.oauth2.springsecurityoauth2practice.sec1springsecurityfundamentals.authenticationentrypoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
//@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(config -> config
                        .anyRequest().authenticated()
                )
                .httpBasic(config -> config.disable())
                .formLogin(Customizer.withDefaults())
                /*
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(new AuthenticationEntryPoint() {
                            @Override
                            public void commence(
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    AuthenticationException authException
                            ) throws IOException, ServletException {
                                log.info("custom entrypoint");
                            }
                        })
                )
                */

                .build();
    }
}
