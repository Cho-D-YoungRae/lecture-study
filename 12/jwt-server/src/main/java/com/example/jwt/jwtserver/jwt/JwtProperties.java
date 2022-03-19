package com.example.jwt.jwtserver.jwt;

public interface JwtProperties {

    String SECRET = "cos"; // 우리 서버만 알고있는 비밀값

    int EXPIRATION_TIME = 10 * 60 * 1000;

    String TOKEN_PREFIX = "Bearer ";

    String HEADER_STRING = "Authorization";
}
