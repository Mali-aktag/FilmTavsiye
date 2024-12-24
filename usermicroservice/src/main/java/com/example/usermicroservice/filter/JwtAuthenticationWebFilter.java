package com.example.usermicroservice.filter;

import com.example.usermicroservice.service.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationWebFilter extends AuthenticationWebFilter {

    public JwtAuthenticationWebFilter(JwtUtil jwtUtil) {
        super(createAuthenticationManager(jwtUtil));
        setServerAuthenticationConverter(exchange -> convert(jwtUtil, exchange));
        setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());
    }

    private static ReactiveAuthenticationManager createAuthenticationManager(JwtUtil jwtUtil) {
        return authentication -> {
            String token = (String) authentication.getCredentials();
            Claims claims = jwtUtil.validateToken(token);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    username, null, List.of(new SimpleGrantedAuthority("ROLE_" + role))
            );
            return Mono.just(auth);
        };
    }

    private static Mono<Authentication> convert(JwtUtil jwtUtil, ServerWebExchange exchange) {
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (token == null || !token.startsWith("Bearer ")) {
            return Mono.empty();
        }

        try {
            String jwt = token.substring(7); // Remove "Bearer " prefix
            Claims claims = jwtUtil.validateToken(jwt);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);
            return Mono.just(new UsernamePasswordAuthenticationToken(
                    username, null, List.of(new SimpleGrantedAuthority("ROLE_" + role))
            ));
        } catch (Exception e) {
            return Mono.empty();
        }
    }
}
