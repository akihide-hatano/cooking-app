package com.example.cookingapp.repository;

import com.example.cookingapp.entity.FamilyMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {

  List<FamilyMember> findByUserIdAndDeletedAtIsNull(Long userId);

  boolean existsByUserIdAndDeletedAtIsNull(Long userId);
}
