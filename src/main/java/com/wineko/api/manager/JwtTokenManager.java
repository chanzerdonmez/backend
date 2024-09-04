package com.wineko.api.manager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Component
public class JwtTokenManager {

    private static final String SECRET_KEY = "wzUpGa9k4LTV3QHuY8qVrt6wOENkvdes5vLHVc1ex6581IiQ";

    public static String generateToken(String usersEmail, Integer id, String role) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        SecretKey key = secretKey();

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims) // Utilisez les claims définis
                .setSubject(usersEmail)
                .setAudience(String.valueOf(id))
                .setExpiration(calendar.getTime())
                .signWith(key, SignatureAlgorithm.HS384)
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
//        return Integer.parseInt(claims.getSubject());
        return Integer.parseInt(claims.getAudience()); // Assurez-vous que l'Audience contient l'ID utilisateur

//        return Integer.parseInt(claims.getAudience()); // Use audience claim for user ID

    }



    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return parseToken(token).getExpiration();
    }

    private static SecretKey secretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
