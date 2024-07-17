package com.wineko.api.manager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;

@Component
public class JwtTokenManager {

    private static final String SECRET_KEY = "wzUpGa9k4LTV3QHuY8qVrt6wOENkvdes5vLHVc1ex6581IiQ";

    public static String generateToken(String tokenUser) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        SecretKey key = secretKey();

        return Jwts.builder()
                .setSubject(tokenUser)
                .setExpiration(calendar.getTime())
                .signWith(key)
                .compact();
    }

    public static Claims parseToken(String token) {
        SecretKey key = secretKey();

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Integer getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Integer.parseInt(claims.getSubject());
    }

    private static SecretKey secretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
