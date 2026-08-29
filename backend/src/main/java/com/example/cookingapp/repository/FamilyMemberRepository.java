package com.example.cookingapp.repository;

import com.example.cookingapp.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {}
