package com.example.gatewayservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Secret Key (Base64 Encoded)
    private final String secret = "my-secret-key-my-secret-key-my-secret-key"; // En az 256 bit
    private final long expiration = 3600000; // 1 saat

    // SecretKey oluşturma
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Token oluşturma
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey()) // Yeni yöntem
                .compact();
    }

    // Token doğrulama ve çözümleme
    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()) // Yeni yöntem
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
