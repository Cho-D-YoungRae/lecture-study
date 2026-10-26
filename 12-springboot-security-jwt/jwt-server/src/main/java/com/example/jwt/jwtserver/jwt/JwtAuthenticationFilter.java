package com.example.jwt.jwtserver.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.jwtserver.auth.PrincipalDetails;
import com.example.jwt.jwtserver.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

// 시큐리티 UsernamePasswordAuthenticationFilter 가 있음
// POST: /login 요청으로 username, password 전송하면 위 필터가 동작을 한다.
// 우리는 formLogin 을 disable 해서 자동으로 동작하지 않는다.
// 직접 이 필터를 다시 Security Filter Chain 에 등록하여 동작하게 한다.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    /**
     * 간단한 로그인 로직
     * 1. username, password 받아서
     * 2. 정상인지 로그인 시도를 확인 -> authenticationManager 로 로그인 시도를 하면
     * 3. PrincipalDetailsService.loadUserByUsername() 실행 -> PrincipalDetails 반환
     * 4. PrincipalDetails 를 세션에 담고
     * -> 세션에 담아주지 않으면 권한이 필요한 요청 거부당함. 세션에 값이 있어야 시큐리티가 권한관리 해줌. 권한관리가 필요 없으면 세션 사용 안해도 됨
     * 5. JWT 토큰을 만들어서 응답을 해준다.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter.attemptAuthentication -> 로그인 시도중");

        // 1. username, password 받아서
        try {
            // 입력된 형태 그대로 문자열로 가져온다
            // ex1) JSON 형태 그대로 문자열
            // ex2) form 입력 형태 그대로 username=ID&password=PW
            /*BufferedReader br = request.getReader();

            String input;
            while ((input = br.readLine()) != null) {
                System.out.println(input);
            }*/

            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println("user = " + user);

            // 로그인 시도를 하려면 토큰을 만들어야 함 -> form 로그인을 하면 자동으로 해주는 것
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // PrincipalDetails.loadUserByUsername() 실행
            // => 정상이면 authentication 반환 -> DB 에 있는 username 과 password 가 일치 -> 인증
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 완료됨? => principalDetails.getUser() = " + principalDetails.getUser());

            // 반환된 authentication 객체가 session 영역에 저장됨. => 로그인이 되었다는 뜻.
            // session 에 저장하는 이유: security 가 대신 권한관리를 해주도록 하려고
            // JWT 사용하면 굳이 session 이용할 이유 없음 -> 권한처리를 위해 넣어준 것
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("=====================================================");
        }

        // 이 메서드 (attemptAuthentication) 가 실행되고 나서 실행되는 메서드 (successfulAuthentication)
        // attemptAuthentication -> successfulAuthentication
        return null;
    }

    // attemptAuthentication() 에서 인증이 정상적으로 되었으면 아래 메서드 (successfulAuthentication) 실행
    // 여기서 JWT 만들어서 request 요청한 사용자에게 response
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        System.out.println("인증 완료 => JwtAuthenticationFilter.successfulAuthentication");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // HMAC256 암호화 방식 => 시크릿 키를 가져야 한다 -> 여기서 시크릿 키 = "cos"
        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", principalDetails.getUser().getId())    // withClaim : 넣고 싶은 값들
                .withClaim("username", principalDetails.getUsername())  // username 은 필요 없을 수도...
                .sign(Algorithm.HMAC256(JwtProperties.SECRET));    // "cos" => 시크릿 키

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
    }
}
