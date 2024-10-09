package com.leoric.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtProvider {
    private final JwtConstant jwtConstant;

    @Autowired
    public JwtProvider(JwtConstant jwtConstant) {
        this.jwtConstant = jwtConstant;
    }

    public String generateToken(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String authoritiesString = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtConstant.getJwtExpiration())) // Use jwtExpiration
                .claim("email", authentication.getName())
                .claim("authorities", authoritiesString)
                .signWith(getSecretKey(jwtConstant.getSecretKey()))
                .compact();
    }

    // Extract email from token
    public String getEmailFromToken(String token) {
        String resolvedToken = resolveToken(token);
        SecretKey key = getSecretKey(jwtConstant.getSecretKey());
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(resolvedToken)
                .getBody();
        return String.valueOf(claims.get("email"));

    }

    // Optionally: Extract authorities from token
    public String getAuthoritiesFromToken(String token) {
        String resolvedToken = resolveToken(token);
        SecretKey key = getSecretKey(jwtConstant.getSecretKey());
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(resolvedToken).getBody();
        return String.valueOf(claims.get("authorities"));
    }

    private String resolveToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // Remove "Bearer " prefix
        }
        return token;
    }

    private SecretKey getSecretKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
