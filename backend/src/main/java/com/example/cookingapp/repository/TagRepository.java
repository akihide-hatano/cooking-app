package com.example.cookingapp.repository;

import com.example.cookingapp.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {}
