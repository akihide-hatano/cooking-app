package com.example.cookingapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "family_members")
@EntityListeners(AuditingEntityListener.class)
public class FamilyMember {

  // id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // user_id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  // family_id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "family_id", nullable = false)
  private Family family;

  // roleはenumで定義する
  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false, length = 20)
  private FamilyMemberRole role;

  // createdAt
  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  // deletedAt
  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;
}
