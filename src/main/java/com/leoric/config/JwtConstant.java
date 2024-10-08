package com.leoric.config;

import org.springframework.beans.factory.annotation.Value;

public class JwtConstant {
    public static final Long jwtExpiration = 3600000L;
    public static final String SECRET_KEY = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
    public static final String JWT_HEADER = "Authorization";
}
