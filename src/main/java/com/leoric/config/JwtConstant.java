package com.leoric.config;

import org.springframework.beans.factory.annotation.Value;

public class JwtConstant {
    @Value("${security.jwt.secret-key}")
    public static String SECRET_KEY;
    public static final String JWT_HEADER = "Authorization";
//    @Value("${security.jwt.expiration-time}")
//    private long jwtExpiration;
}
