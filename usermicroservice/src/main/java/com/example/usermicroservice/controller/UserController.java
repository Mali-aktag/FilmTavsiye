package com.example.usermicroservice.controller;

import com.example.usermicroservice.model.User;
import com.example.usermicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Kullanıcıyı kullanıcı adına göre getir
    @GetMapping("/{username}")
    public Mono<User> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    // Yeni kullanıcı oluştur
    @PostMapping
    public Mono<User> createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // Kullanıcıyı güncelle
    @PutMapping("/{id}")
    public Mono<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    // Kullanıcıyı sil
    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    // Tüm kullanıcıları listele
    @GetMapping
    public Flux<User> listAllUsers() {
        return userService.listAllUsers();
    }
}
