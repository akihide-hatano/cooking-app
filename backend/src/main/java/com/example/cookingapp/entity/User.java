package com.example.cookingapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    // ① id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ② name
    @Column(name = "name", nullable = false,length = 50)
    private String name;

    // ③ email
    @Column(name = "email", nullable = false, unique = true,length = 254)
    private String email;

    // ④ passwordHash
    @Column(name = "password_hash", nullable = false,length = 255)
    private String passwordHash;

    // ⑤ createdAt
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ⑥ updatedAt
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ⑦ deletedAt
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}