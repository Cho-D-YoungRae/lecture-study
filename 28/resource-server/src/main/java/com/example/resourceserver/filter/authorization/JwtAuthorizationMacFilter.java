package com.example.resourceserver.filter.authorization;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;


/**
 * JwtDecoder(Spring) 을 사용하면 간단하게 해결 가능
 * 아래는 직접 구현해본 것
 */
@RequiredArgsConstructor
public class JwtAuthorizationMacFilter extends OncePerRequestFilter {

    // JWK 타입으로 받지 않은 이유는 이거는 공통 클래스라기보다는 MAC 에 국한된 클래스 이므로
    private final OctetSequenceKey jwk;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            // 헤더에 값이 없는 경우 여기 필터에서는 처리할게 없으므로 다음으로 넘어감
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");
        SignedJWT signedJWT;

        try {
            signedJWT = SignedJWT.parse(token);
            MACVerifier macVerifier = new MACVerifier(jwk.toSecretKey());
            boolean verify = signedJWT.verify(macVerifier);

            if (verify) {
                JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
                String username = jwtClaimsSet.getClaim("username").toString();
                List<String> authority = (List) jwtClaimsSet.getClaim("authority");

                if (username != null) {
                    UserDetails user = User.withUsername(username)
                            .password(UUID.randomUUID().toString())
                            .authorities(authority.get(0))
                            .build();

                    Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                            user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request, response);
    }
}
