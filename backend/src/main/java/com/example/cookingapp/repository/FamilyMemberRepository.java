package com.example.cookingapp.repository;

import com.example.cookingapp.entity.FamilyMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {

  List<FamilyMember> findByUserId(Long userId);

  boolean existsByUserId(Long userId);
}
