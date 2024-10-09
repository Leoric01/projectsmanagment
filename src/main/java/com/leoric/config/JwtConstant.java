package com.leoric.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class JwtConstant {
    @Value("${security.jwt.expiration-time}")
    private Long jwtExpiration;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    public static final String JWT_HEADER = "Authorization";
}
