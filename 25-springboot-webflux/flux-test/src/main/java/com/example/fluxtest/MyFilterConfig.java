package com.example.fluxtest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MyFilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> addFilter1(EventNotify eventNotify) {
        log.info("필터 등록됨 - 1");
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(new MyFilter(eventNotify));
        bean.addUrlPatterns("/sse");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<Filter> addFilter2(EventNotify eventNotify) {
        log.info("필터 등록됨 - 2");
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(new MyFilter2(eventNotify));
        bean.addUrlPatterns("/add");
        return bean;
    }
}
