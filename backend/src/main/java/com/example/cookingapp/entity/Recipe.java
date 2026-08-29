package com.example.cookingapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "recipes")
@EntityListeners(AuditingEntityListener.class)
public class Recipe {

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

  // name
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  // description
  @Column(name = "description", length = 500)
  private String description;

  // image_url
  @Column(name = "image_url", length = 500)
  private String imageUrl;

  // cookedDate
  @Column(name = "cooked_date", nullable = false)
  private LocalDate cookedDate;

  // visibility
  @Enumerated(EnumType.STRING)
  @Column(name = "visibility", nullable = false, length = 20)
  private RecipeVisibility visibility;

  // createdAt
  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  // updatedAt
  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  // deletedAt
  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;
}
