package com.example.jwt.jwtserver.filter;

import javax.servlet.*;
import java.io.IOException;

public class MyFilter4 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("필터4");
        chain.doFilter(request, response);
    }
}
