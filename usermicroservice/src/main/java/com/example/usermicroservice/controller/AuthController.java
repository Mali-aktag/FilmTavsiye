package com.example.usermicroservice.controller;

import com.example.usermicroservice.model.User;
import com.example.usermicroservice.service.JwtUtil;
import com.example.usermicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody User user) {
        return userService.findByUsername(user.getUsername())
                .flatMap(existingUser -> {
                    // BCrypt ile şifre karşılaştırması
                    if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                        String token = jwtUtil.generateToken(existingUser.getUsername(), existingUser.getRole().name());
                        return Mono.just(ResponseEntity.ok(token));
                    }
                    return Mono.just(ResponseEntity.badRequest().body("Invalid credentials"));
                })
                .defaultIfEmpty(ResponseEntity.badRequest().body("User not found"));
    }

}
