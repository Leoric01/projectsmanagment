package com.leoric.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.leoric.config.JwtConstant.jwtExpiration;

public class JwtProvider {
    static SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
    @Value("${security.jwt.expiration-time}")


    public static String generateToken(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String authoritiesString = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpiration))
                .claim("email", authentication.getName())
                .claim("authorities", authoritiesString)
                .signWith(key)
                .compact();
    }
    public static String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return String.valueOf(claims.get("email"));
    }

}
