package com.javizs.store.games.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "rol")
    @Enumerated(EnumType.STRING)
    private Role rol;

    @Column(name = "active")
    private boolean active = true;

    public User(String name, String email, String password, String phone, Role rol) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.rol = rol;
        this.active = true;
    }
}