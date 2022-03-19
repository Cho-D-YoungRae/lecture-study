package com.example.jwt.jwtserver.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.jwtserver.auth.PrincipalDetails;
import com.example.jwt.jwtserver.model.User;
import com.example.jwt.jwtserver.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

// 시큐리티가 filter 가지고 있는데 그 filter 중에 BasicAuthenticationFilter 이라는 것이 있음
// 인증/권한이 필요한 특정 주소를 요청하면 위 filter 를 무조건 타게 되어있음
// 만약 인증/권한이 필요한 주소가 아니라면 위 filter 안 탐.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    // 인증/권한이 필요한 주소요청이 있을 때 해당 필터를 타게 됨.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("권한이나 인증이 필요한 주소 요청");

        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
        System.out.println("jwtHeader = " + jwtHeader);

        // header 가 정상적으로 있는지 확인
        if (Objects.isNull(jwtHeader) || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX.trim())) {
            chain.doFilter(request, response);
            return;
        }
        // JWT 토큰을 검증해서 정상적인 사용자인지 확인
        String jwtToken = jwtHeader.replace(JwtProperties.TOKEN_PREFIX, "");
        String username = JWT.require(Algorithm.HMAC256(JwtProperties.SECRET)).build()
                .verify(jwtToken).getClaim("username").asString();

        // 서명이 정상적으로 됨
        if (Objects.nonNull(username)) {
            User user = userRepository.findByUsername(username).get();

            PrincipalDetails principalDetails = new PrincipalDetails(user);

            // AuthenticationManager.authenticate() 로 Authentication 객체 만들 수 있지만 그것은 실제 로그인 할 때
            // 강제로 만들어 줄 때는 아래와 같이 가능
            // credentials (password) = null -> 실제 로그인 하는 것이 아니라 강제로 Authentication 만드는 것이므로 없어도 된다.
            // => username != null 이므로 사용자가 정상적으로 인증이 된 것 -> Authentication 강제로 만들어 주어도 된다.
            // JWT 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어 준다.
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principalDetails, null, principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
