package com.example.usermicroservice.service;

import com.example.usermicroservice.model.User;
import com.example.usermicroservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Kullanıcıyı kullanıcı adına göre bul
    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Yeni kullanıcı ekle
    public Mono<User> saveUser(User user) {
        return userRepository.save(user);
    }

    // Kullanıcı güncelle
    public Mono<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setUsername(updatedUser.getUsername());
                    existingUser.setPassword(updatedUser.getPassword());
                    existingUser.setRole(updatedUser.getRole());
                    return userRepository.save(existingUser);
                });
    }

    // Kullanıcıyı sil
    public Mono<Void> deleteUser(Long id) {
        return userRepository.deleteById(id);
    }

    // Tüm kullanıcıları listele
    public Flux<User> listAllUsers() {
        return userRepository.findAll();
    }
}
