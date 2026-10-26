package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static java.util.Objects.isNull;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whileList = {"/", "/login", "/members/add", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        try {
            log.info("인증 체크 필터 시작 {}", requestURI);

            if (isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if (isNull(session) || isNull(session.getAttribute(SessionConst.LOGIN_MEMBER))) {
                    log.info("미인증 사용자 요청 {}", requestURI);
                    // 로그인으로 redirect
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);

                    return; // 미인증 사용자의 경우 다음 서블릿이나 컨트롤러 호출하지 않도록
                }
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            // 예외 로깅 가능 하지만, 톰캣까지 예외를 보내주어야함
            // 여기서 예외를 먹어버리면 정상흐름으로 간주됨
            throw e;
        } finally {
            log.info("인증 체크 필터 종료");
        }
    }

    /**
     * 화이트 리스트의 경우 인증체크 X
     */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whileList, requestURI);
    }
}
