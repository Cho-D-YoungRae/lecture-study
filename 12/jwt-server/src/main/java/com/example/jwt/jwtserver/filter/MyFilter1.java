package com.example.jwt.jwtserver.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("필터1");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 토큰: cos -> 이걸 만들어줘야 함. ID, PW 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 그걸 응답해준다.
        // 요청할 때 마다 header 에 Authorization 에 value 값으로 토큰을 가지고 오겠죠??
        // 그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지만 검증하면 됨. -> RSA or HS256
        // 이런 Filter 는 Security Filter 이전에 실행이 되어야 한다.
        /*
        if ("POST".equals(req.getMethod())) {
            System.out.println("POST 요청됨");
            String headerAuth = req.getHeader("Authorization");
            System.out.println("headerAuth = " + headerAuth);

            if ("cos".equals(headerAuth)) {
                chain.doFilter(req, res);
            } else {
                PrintWriter writer = res.getWriter();
                writer.println("인증안됨");
            }
        }
        */
        chain.doFilter(req, res);
    }
}
