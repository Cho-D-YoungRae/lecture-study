package com.example.fluxtest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class MyFilter implements Filter {

    private final EventNotify eventNotify;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("Filter 실행");
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        /**
         * text/plain 의 경우 버퍼를 계속 비워줘도 나중에 한번에 전송됨
         */
//        servletResponse.setContentType("text/plain;charset=utf-8");
        servletResponse.setContentType("text/event-stream;charset=utf-8");

        PrintWriter out = servletResponse.getWriter();
        // 1. Reactive Streams 라이브러리를 쓰면 표준을 지켜서 응답 할 수 있음
        // 여기만 있으면 소비가 끝나면 종료
        for (int i = 0; i < 5; i++) {
            out.print("응답-" + i + "\n");
            out.flush();    // 버퍼를 비움
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 2. SSE Emitter 라이브러리를 사용하면 편하게 쓸 수 있음
        // 소비가 끝나도 종료되지 않음
        while (true) {
            try {
                if (!eventNotify.getEvents().isEmpty()) {
                    out.print("응답-" + eventNotify.getEvents().poll() + "\n");
                    out.flush();
                }
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 3. WebFlux -> Reactive Streams 이 친구가 적용된 stream을 배우고 (비동기 단일스레드 동작)
        // 4. Servlet MVC -> Reactive Streams 적용 가능하지만 (멀티스레드 방식)
    }
}
