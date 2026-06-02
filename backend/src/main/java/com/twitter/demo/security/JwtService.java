package com.twitter.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;



/*
 * Secret key koddan çıkarıldı, environment variable’a taşındı.
 * `application.properties` içine env okumaları eklendi.
 * `@Value` ile Spring env değerlerini inject etti.
 * `static final` kaldırıldı çünkü Spring runtime’da değer verir.
 * IntelliJ Run Configuration’a `JWT_SECRET` eklendi.
 * Uygulama artık SaaS ve production ortamlarında güvenli çalışabilir hale geldi.

 */

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 saat

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    // login durumunda token üretmek için bu methodu kullanacağız. AuthServiceImp içinde.
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    // Filter içerisinde token doğrulama yaparken bu methodu kullanacağız. JwtFilter içinde.
    public String extractUsername(String token) {
        return extractAllClaims(token)
                .getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
