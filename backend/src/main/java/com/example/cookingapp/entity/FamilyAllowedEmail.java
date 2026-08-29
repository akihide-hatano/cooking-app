package com.example.cookingapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "family_allowed_emails")
@EntityListeners(AuditingEntityListener.class)
public class FamilyAllowedEmail {

  // ① id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // ② family_id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "family_id", nullable = false)
  private Family family;

  // ③ email
  @Column(name = "email", nullable = false, length = 254)
  private String email;

  // ④create_by:user_id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", nullable = false)
  private User createdBy;

  // ④ createdAt
  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  // ⑤ deletedAt
  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;
}
