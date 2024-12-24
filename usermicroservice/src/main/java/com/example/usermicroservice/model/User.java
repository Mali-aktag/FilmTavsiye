package com.example.usermicroservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("users") // Tablo adı
public class User {

    @Id
    @Column("ID") // Veritabanındaki sütun adı ile tam eşleşme
    private Long id;

    @Column("USERNAME") // Sütun adı
    private String username;

    @Column("PASSWORD") // Sütun adı
    private String password;

    @Column("ROLE") // Sütun adı
    private Role role;

    // Getter ve Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
