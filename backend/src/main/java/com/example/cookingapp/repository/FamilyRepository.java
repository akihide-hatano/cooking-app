package com.example.cookingapp.repository;

import com.example.cookingapp.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyRepository extends JpaRepository<Family, Long> {}
