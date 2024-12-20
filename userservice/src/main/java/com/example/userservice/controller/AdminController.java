package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // Tüm kullanıcıları listeleme
    @GetMapping("/users")
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // Kullanıcıyı ID ile silme
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Kullanıcı bulunamadı.");
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("Kullanıcı başarıyla silindi.");
    }

    // Kullanıcı bilgilerini güncelleme (Şifre hariç)
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        User existingUser = userOptional.get();
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setRole(updatedUser.getRole());
        userRepository.save(existingUser);
        return ResponseEntity.ok(existingUser);
    }

    // Kullanıcının rolünü değiştirme
    @PatchMapping("/users/{id}/role")
    public ResponseEntity<User> updateUserRole(@PathVariable Long id, @RequestParam String role) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        User user = userOptional.get();
        user.setRole(role);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    // Kullanıcıyı ID ile detaylı görüntüleme
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(userOptional.get());
    }

    // Kullanıcıyı kullanıcı adı ile detaylı görüntüleme
    @GetMapping("/users")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(userOptional.get());
    }
}
