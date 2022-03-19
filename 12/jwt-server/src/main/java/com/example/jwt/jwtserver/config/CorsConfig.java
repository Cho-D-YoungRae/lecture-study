package com.example.jwt.jwtserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    // 이 필터를 SecurityConfig 에서 등록해주어서 cors 해결
    // @CrossOrigin 은 인증이 없을 때 사용하는 것
    // 인증이 있을 때는 시큐리티 필터에 등록을 해주어야 한다.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);   // 내 서버가 응답을 할 때 json 을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것 -> 자바스크립트의 특정 라이브러리로 요청할 때
        config.addAllowedOrigin("*");   // 모든 ip 의 응답을 허용
        config.addAllowedHeader("*");   // 모든 header 의 응답을 허용
        config.addAllowedMethod("*");   // 모든 http method 요청을 허용
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
