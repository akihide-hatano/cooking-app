# レシピの論理削除・公開範囲・例外処理に関する調査記録

- 調査日：2026-10-31
- 対象：料理投稿アプリ（Spring Boot / Spring Data JPA）
- 関連Issue：#22 レシピ削除機能
- ステータス：静的コード調査完了・動作検証未完了

## 1. 調査目的

レシピの作成・取得・削除について、以下の仕様がどのように実装されているかを確認する。

1. `created_at`、`updated_at`、`deleted_at` の管理方法
2. 論理削除後のデータ取得制限
3. PUBLIC / FAMILY / PRIVATE の閲覧権限制御
4. 例外発生時のHTTPステータス
5. 一覧取得と詳細取得における認可ロジックの整合性

本調査ではソースコードから実装を確認した。API実行結果やDB更新結果の検証は別途実施する。

## 2. 調査対象ファイル

| ファイル | 調査内容 |
|---|---|
| `entity/Recipe.java` | Entity、日時カラム、JPA監査 |
| `config/JpaConfig.java` | JPA監査の有効化 |
| `repository/RecipeRepository.java` | 論理削除・公開範囲の検索条件 |
| `service/RecipeService.java` | 一覧・詳細・削除処理 |
| `controller/RecipeController.java` | DELETE API |
| `exception/GlobalExceptionHandler.java` | 例外とHTTPレスポンスの対応 |

## 3. 日時カラムの実装

### 3.1 作成日時・更新日時

`Recipe.java` に以下の定義がある。

```java
@CreatedDate
@Column(name = "created_at", nullable = false, updatable = false)
private LocalDateTime createdAt;

@LastModifiedDate
@Column(name = "updated_at", nullable = false)
private LocalDateTime updatedAt;
```

また、Entityには次の設定がある。

```java
@EntityListeners(AuditingEntityListener.class)
```

`JpaConfig.java` には `@EnableJpaAuditing` が存在する。

**調査結果**

- Spring Data JPAの監査機能によって作成日時・更新日時を自動設定する設計である。
- 自動設定に必要な主要な設定は確認できた。
- 実際にINSERT・UPDATE時に日時が保存されるかは未検証。

### 3.2 削除日時

`Recipe.java` の定義は以下のとおり。

```java
@Column(name = "deleted_at")
private LocalDateTime deletedAt;
```

`RecipeService.deleteRecipe()` では以下の処理を実行する。

```java
recipe.setDeletedAt(LocalDateTime.now());
recipeRepository.save(recipe);
```

**調査結果**

- 物理削除ではなく、`deleted_at` に日時を設定する論理削除方式を採用している。
- 作成日時はJPA監査、削除日時はServiceで明示的に設定する方式である。
- 両者の実装方法が異なること自体は不具合ではない。
- 日時のタイムゾーン方針は今回の調査では未確認。

## 4. Repositoryの論理削除条件

`RecipeRepository.java` には以下のメソッドが存在する。

```java
List<Recipe> findAllByDeletedAtIsNull();

Optional<Recipe> findByIdAndDeletedAtIsNull(Long id);
```

また、一覧取得用の `findVisibleRecipes()` には次の条件がある。

```sql
WHERE r.deletedAt IS NULL
```

**調査結果**

- 削除済みレシピを検索対象から除外する条件が存在する。
- `findAllByDeletedAtIsNull()` は今回確認したJavaソース内で呼び出し箇所が見つからなかった。
- 一覧取得のServiceでは `findVisibleRecipes()` が使用されている。
- 他の取得経路も含め、削除済みレシピが返らないことはAPIテストで確認する必要がある。

## 5. 一覧取得の公開範囲制御

`RecipeService.getAllRecipes()` では以下の順序で処理している。

1. `SecurityContextHolder` から認証情報を取得する。
2. PrincipalからユーザーIDを取得する。
3. 有効な家族所属情報を取得する。
4. 所属家族IDの一覧を生成する。
5. `findVisibleRecipes()` にユーザーID・家族ID・公開範囲を渡す。

Repositoryの取得条件は以下のとおり。

| 公開範囲 | 取得条件 |
|---|---|
| PUBLIC | 公開レシピ |
| FAMILY | ユーザーが所属する家族のレシピ |
| PRIVATE | ユーザー自身が作成したレシピ |

全ての条件に `deletedAt IS NULL` が適用される。

### 家族未所属時の処理

```java
List<Long> queryFamilyIds =
    familyIds.isEmpty() ? List.of(-1L) : familyIds;
```

家族IDが空の場合、`IN` 句に空リストを渡さないための対策が実装されている。

ただし、`-1` が実在しない家族IDであることを前提としている。

**調査結果**

一覧取得には、論理削除と公開範囲を組み合わせた検索条件が実装されている。

認証Principalの形式、家族所属情報の正当性、実際の検索結果は動作テストで検証する。

## 6. 詳細取得の公開範囲制御

`RecipeService.getRecipe()` では以下を実施している。

- `findByIdAndDeletedAtIsNull()` によるレシピ取得
- PRIVATEの場合、作成者本人かどうかを判定
- FAMILYの場合、有効な家族所属情報を取得して判定
- 閲覧できない場合、`RecipeNotFoundException` を送出

### 発見した設計上の懸念

一覧取得と詳細取得では、同じ閲覧権限ルールを異なる方法で実装している。

| 処理 | 認可判定の場所 | 方法 |
|---|---|---|
| 一覧取得 | Repository | JPQLによる条件絞り込み |
| 詳細取得 | Service | Javaの条件分岐・ループ |

実装方法が異なること自体は問題ではない。

しかし、同じ業務ルールが複数箇所に分散しているため、将来の仕様変更時に片方だけ変更されるリスクがある。

例えば、FAMILYの閲覧条件を変更した場合、一覧と詳細で表示可否が異なる可能性がある。

**改善方針**

- 公開範囲の判定ルールを明文化する。
- 一覧・詳細に共通する認可テストケースを用意する。
- 必要に応じて認可判定の責務を整理する。
- 単純なメソッド共通化を目的化せず、検索効率と認可の一貫性を両立する。

## 7. DELETE処理の調査

`RecipeController` には `@DeleteMapping("/{id}")` が存在し、成功時にHTTP 204を返す実装がある。

`RecipeService.deleteRecipe()` は以下の処理を行う。

1. 未削除のレシピをIDで取得する。
2. 認証ユーザーIDを取得する。
3. レシピ作成者との一致を確認する。
4. `deletedAt` に現在日時を設定する。
5. Repositoryで保存する。

**確認できたこと**

- 削除対象の存在確認がある。
- 作成者本人のチェックがある。
- 論理削除処理がある。
- 成功時の204レスポンスが定義されている。

**未検証事項**

- 実際にDBへ削除日時が保存されるか。
- 他人のレシピを削除できないか。
- 削除済みレシピへの再DELETEがどう扱われるか。
- 削除後に一覧・詳細から除外されるか。
- 関連するレシピ手順などの扱いが仕様と一致するか。

## 8. 例外処理の調査

`GlobalExceptionHandler` には以下の対応がある。

| 例外クラス | HTTPステータス |
|---|---|
| `IllegalArgumentException` | 400 Bad Request |
| `MethodArgumentNotValidException` | 400 Bad Request |
| `RecipeNotFoundException` | 404 Not Found |

一方、Serviceでは以下の違いがある。

| 処理 | レシピが見つからない場合 |
|---|---|
| GET詳細 | `RecipeNotFoundException` |
| UPDATE | `IllegalArgumentException` |
| DELETE | `IllegalArgumentException` |

### 発見した問題

同じ「レシピが存在しない・削除済み」という状態でも、処理によって例外クラスとHTTPステータスが異なる。

また、DELETEでは権限不足の場合にも `IllegalArgumentException` を使用している。

そのため、入力不正・対象不存在・権限不足という異なる失敗理由が400にまとめられている。

### 改善候補

- 対象不存在と削除済みを404に統一する。
- 他人のレシピへのアクセスは、存在を秘匿する404、または認可失敗を明示する403のどちらかを仕様として決定する。
- 未認証時の401を確認する。
- エラーレスポンス形式の統一を検討する。
- 既存のUPDATE処理への影響は別途判断する。

**注意**

現時点で確認したのは例外クラスとハンドラの実装であり、実際のHTTPレスポンスは未検証である。

## 9. 今後のテスト観点

| No. | テスト条件 | 期待結果 |
|---|---|---|
| 1 | レシピ新規作成 | `created_at` が設定される |
| 2 | レシピ更新 | `updated_at` が更新される |
| 3 | 作成者本人がDELETE | HTTP 204、`deleted_at` 設定 |
| 4 | DELETE後に詳細取得 | HTTP 404 |
| 5 | DELETE後に一覧取得 | 対象レシピが含まれない |
| 6 | 存在しないIDをDELETE | 仕様で定めた404 |
| 7 | 削除済みIDを再DELETE | 仕様で定めた404 |
| 8 | 他人のレシピをDELETE | 仕様に応じた403または404、DB変更なし |
| 9 | 未認証でDELETE | HTTP 401 |
| 10 | 他家族のFAMILYレシピを取得 | 一覧・詳細とも非公開 |
| 11 | 他人のPRIVATEレシピを取得 | 一覧・詳細とも非公開 |
| 12 | 家族未所属で一覧取得 | PUBLICと自身のPRIVATEのみ取得 |

上記の期待結果は、必要な仕様決定を行ったうえでテストとして確定する。

## 10. 対応方針

### Issue #22で対応する範囲

- DELETEの例外処理とHTTPステータスを整理する。
- 作成者以外による削除を防ぐ。
- 論理削除の正常系・異常系をテストする。
- React側に削除確認・削除実行・エラー表示を実装する。
- 削除後の画面遷移と一覧・詳細への反映を確認する。

### 別Issueで検討する範囲

- 一覧・詳細の認可ルールの一貫性確保
- 公開範囲ごとの認可テスト拡充
- UPDATE・DELETEの例外設計統一
- 日時・タイムゾーンの管理方針
- 未使用Repositoryメソッドの整理

## 11. 今回の学び

今回の調査では、以下を確認した。

1. `grep` は実装箇所の検索、`sed` は指定範囲の確認に使える。
2. `@CreatedDate` と `@EnableJpaAuditing` はJPA監査による日時設定に関係する。
3. `deletedAt` はServiceで明示的に設定して論理削除する方式である。
4. 論理削除は、保存処理だけでなく全ての取得経路での除外条件が重要になる。
5. `@RestControllerAdvice` は例外クラスに応じてHTTPレスポンスを決定する。
6. 同じ認可ルールが複数箇所に分散すると、仕様変更時に不一致が生じるリスクがある。
7. コード上の実装確認と、テストによる動作証明は区別する必要がある。

## 12. 結論

レシピの作成日時・論理削除・一覧の公開範囲制御には、必要な主要実装が存在することを確認した。

一方、例外処理の不統一と、一覧・詳細に分散した認可ロジックを改善候補として発見した。

今後はIssue #22の削除機能を完成させ、正常系・異常系のテストによって実装の正しさを検証する。