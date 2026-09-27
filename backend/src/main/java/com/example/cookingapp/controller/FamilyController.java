package com.example.cookingapp.controller;

import com.example.cookingapp.dto.CreateFamilyRequest;
import com.example.cookingapp.dto.CreateFamilyRespose;
import com.example.cookingapp.entity.Family;
import com.example.cookingapp.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/families")
@RequiredArgsConstructor
public class FamilyController {

  // 必要なサービスを注入する
  private final FamilyService familyService;

  // postにて家族情報を取得するエンドポイントを作成する
  @PostMapping
  public ResponseEntity<CreateFamilyRespose> createFamily(
      @RequestBody CreateFamilyRequest request) {

    String familyRequest = request.getName();

    Family family = familyService.createFamily(familyRequest);

    // ここで、familyServiceを使って家族情報を取得する処理を実装する
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CreateFamilyRespose(family.getId(), family.getName()));
  }
}
