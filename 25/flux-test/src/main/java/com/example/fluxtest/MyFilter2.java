package com.example.fluxtest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class MyFilter2 implements Filter {

    private final EventNotify eventNotify;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("Filter2 실행");
        // 데이터를 하나 발생시켜서
        eventNotify.add("new Data");
    }
}
