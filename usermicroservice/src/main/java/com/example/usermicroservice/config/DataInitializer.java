package com.example.usermicroservice.config;

import com.example.usermicroservice.model.Role;
import com.example.usermicroservice.model.User;
import com.example.usermicroservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import reactor.core.publisher.Mono;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Admin kullanıcı oluştur
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("adminpassword"));
        admin.setRole(Role.ADMIN);

        // User1 oluştur
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword(passwordEncoder.encode("password1"));
        user1.setRole(Role.USER);

        // User2 oluştur
        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword(passwordEncoder.encode("password2"));
        user2.setRole(Role.USER);

        // Kullanıcıları kaydet
        userRepository.deleteAll() // Önce veritabanını temizle
            .thenMany(
                Mono.just(admin).flatMap(userRepository::save)
                    .then(Mono.just(user1).flatMap(userRepository::save))
                    .then(Mono.just(user2).flatMap(userRepository::save))
            )
            .subscribe(saved -> System.out.println("Başlangıç kullanıcıları eklendi: " + saved));
    }
}
