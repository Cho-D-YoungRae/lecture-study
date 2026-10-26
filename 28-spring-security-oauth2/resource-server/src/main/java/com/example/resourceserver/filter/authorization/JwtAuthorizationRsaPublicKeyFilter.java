package com.example.resourceserver.filter.authorization;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.io.IOException;
import java.util.List;


public class JwtAuthorizationRsaPublicKeyFilter extends JwtAuthorizationFilter {

    private final JwtDecoder jwtDecoder;

    public JwtAuthorizationRsaPublicKeyFilter(JwtDecoder jwtDecoder) {
        super(null);
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (tokenResolve(request)){
            chain.doFilter(request,response);
            return;
        }

        if(jwtDecoder != null) {
            Jwt jwt = jwtDecoder.decode(getToken(request));
            String username = jwt.getClaimAsString("username");
            List<String> authority = jwt.getClaimAsStringList("authority");

            if (username != null) {
                UserDetails user = User.builder().username(username)
                        .password("")
                        .authorities(authority.get(0))
                        .build();
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }
}
