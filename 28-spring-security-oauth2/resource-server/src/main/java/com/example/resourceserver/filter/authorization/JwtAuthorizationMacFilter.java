package com.example.resourceserver.filter.authorization;

import com.nimbusds.jose.JWSVerifier;


/**
 * JwtDecoder(Spring) 을 사용하면 간단하게 해결 가능
 * 아래는 직접 구현해본 것
 */
public class JwtAuthorizationMacFilter extends JwtAuthorizationFilter {

    public JwtAuthorizationMacFilter(JWSVerifier jwsVerifier) {
        super(jwsVerifier);
    }
}
