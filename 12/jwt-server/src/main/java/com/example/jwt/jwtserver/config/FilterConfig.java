package com.example.jwt.jwtserver.config;

import com.example.jwt.jwtserver.filter.MyFilter2;
import com.example.jwt.jwtserver.filter.MyFilter3;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 필터1 -> 필터4 -> 필터2 -> 필터3
// 시큐리티 필터 체인이 먼저 실행된다.
// 내가 만든 필터가 가장 먼저 실행되도록 등록하고 싶으면
// SecurityConfig 에서 addFilterBefore 를 통해 Security Filter Chain 의 가장 앞에 등록
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2() {
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*");
        bean.setOrder(0);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter3> filter3() {
        FilterRegistrationBean<MyFilter3> bean = new FilterRegistrationBean<>(new MyFilter3());
        bean.addUrlPatterns("/*");
        bean.setOrder(1);
        return bean;
    }
}
