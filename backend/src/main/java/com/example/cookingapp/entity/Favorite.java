package com.example.cookingapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@Table(name = "favorites")
@EntityListeners(AuditingEntityListener.class)
public class Favorite {

  // id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // user_id
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  // recipe_id
  @ManyToOne
  @JoinColumn(name = "recipe_id", nullable = false)
  private Recipe recipe;

  // createdAt
  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;
}
