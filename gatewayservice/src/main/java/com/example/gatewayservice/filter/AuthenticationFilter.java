package com.example.gatewayservice.filter;

import com.example.gatewayservice.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements WebFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.error(new RuntimeException("Authorization header is missing or invalid"));
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = jwtUtil.validateToken(token);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Invalid token"));
        }

        String role = claims.get("role", String.class);
        String path = exchange.getRequest().getPath().toString();

        // Role-based route validation
        if (path.startsWith("/api/users") && !"ADMIN".equals(role)) {
            return Mono.error(new RuntimeException("Unauthorized access"));
        } else if (path.startsWith("/api/films") && !("USER".equals(role) || "ADMIN".equals(role))) {
            return Mono.error(new RuntimeException("Unauthorized access"));
        } else if (path.startsWith("/api/comments") && !"ADMIN".equals(role) && !"USER".equals(role)) {
            return Mono.error(new RuntimeException("Unauthorized access"));
        }

        return chain.filter(exchange);
    }
}
