# Issue #22 レシピ削除機能 — Frontend調査・設計判断

## 1. 目的

既存のレシピ編集画面とAPI通信処理を調査し、レシピ削除機能を安全かつ必要最小限の変更で実装するための方針を決定する。

調査では、既存コードの再利用、認証・認可、画面遷移、エラー処理、将来の拡張性を確認する。

## 2. 調査対象

| ファイル | 調査内容 |
|---|---|
| `RecipeEditPage.tsx` | 編集フォーム、状態管理、更新処理、画面遷移 |
| `RecipeDetailPage.tsx` | 詳細画面から編集画面への導線 |
| `api/recipes.ts` | JWT認証、HTTP通信、エラー処理 |
| `types/RecipeDetail.ts` | 詳細APIのレスポンス型 |
| `RecipeDetailResponse.java` | Backendの詳細レスポンスDTO |
| `RecipeController.java` | 詳細取得・更新・削除のエンドポイント |
| `RecipeService.java` | 詳細取得、更新・削除の認可と論理削除 |

## 3. 調査結果

### 3.1 既存の編集画面

`RecipeEditPage.tsx` には、以下が実装されている。

- `useParams` によるレシピID取得と妥当性確認
- `getRecipeDetail()` による既存データ取得
- `useState` によるフォーム状態管理
- `updateRecipe()` による更新処理
- `isSubmitting` による更新中の状態管理
- `saveError` による更新失敗時の表示
- `useNavigate` による更新成功後の詳細画面への遷移
- `isMounted` による画面離脱後の状態更新防止

削除操作は未実装。

### 3.2 API通信

`frontend/src/api/recipes.ts` には、作成・詳細取得・更新のAPI関数が存在する。

共通する処理は以下のとおり。

- `getToken()` によるJWT取得
- `Authorization: Bearer ...` ヘッダーの設定
- HTTP 401でのトークン削除
- HTTPステータスに応じたエラー処理

DELETE用のAPI関数は存在しない。

### 3.3 Backendの削除処理

既存の `DELETE /recipes/{id}` は、`RecipeController` から `RecipeService.deleteRecipe()` を呼び出す。

Serviceでは以下を実行する。

1. 未削除のレシピを取得する。
2. ログインユーザーIDを取得する。
3. レシピ作成者との一致を確認する。
4. `deletedAt` に日時を設定する。
5. Repositoryで保存する。

正常終了時、ControllerはHTTP 204 No Contentを返す。

**確認範囲：ソースコード調査。APIの実動作は未検証。**

### 3.4 権限管理の現状

UPDATEとDELETEの両方で、レシピ作成者のみ操作できるように判定している。

一方、レシピ詳細APIは閲覧権限に基づいて取得できるため、閲覧可能であることは編集・削除可能であることを意味しない。

`RecipeDetailResponse.java` および `RecipeDetail.ts` には、`ownerId`、`canEdit`、`canDelete` が存在しない。

また、UPDATEとDELETEでは、認証ユーザーID取得と所有者チェックが重複している。

### 3.5 HTTPステータスの課題

UPDATE・DELETEでは、レシピが存在しない場合と所有者でない場合に `IllegalArgumentException` を投げている。

調査済みの `GlobalExceptionHandler` では、この例外はHTTP 400として処理される。

Frontendの既存API関数では403・404を個別に扱っているため、APIとFrontendのエラー仕様を整理する必要がある。

## 4. 設計判断

### 4.1 削除専用画面は作成しない

**採用方針：** 既存の `RecipeEditPage.tsx` の下部に削除操作を追加する。

**理由：**

- 編集・削除を管理操作として同じ画面にまとめられる。
- 既存の画面構成とデザインを再利用できる。
- 新規画面やルーティングを増やさずに済む。
- 変更箇所を限定できる。

### 4.2 削除前に確認する

削除ボタンを押した時点ではAPIを実行せず、確認操作を挟む。

キャンセル時は削除しない。

確認後にDELETE APIを実行し、成功時はレシピ一覧画面へ遷移する。

### 4.3 更新と削除の状態を区別する

既存の `isSubmitting` は更新処理に使用する。

削除処理には、`isDeleting` と `deleteError` を追加する方針とする。

更新・削除のどちらかを実行中は、両方の操作を無効化し、同時送信や二重送信を防止する。

### 4.4 DELETE API関数を追加する

`frontend/src/api/recipes.ts` に `deleteRecipe(id)` を追加する。

既存のJWT取得、Authorizationヘッダー、401エラー処理の方式を踏襲する。

HTTP 204 No Contentは本文を返さないため、成功時に `response.json()` は呼び出さない。

### 4.5 `canEdit` / `canDelete` は将来の拡張候補

複雑な権限管理が必要になった場合、Backendで操作可否を計算し、詳細APIに `canEdit` / `canDelete` を含める設計を検討する。

これらはDBに保存するフラグではなく、現在のユーザーとレシピの状態から計算する値とする。

ただし、今回のIssue #22では実装範囲の拡大を避け、採用を保留する。

**重要：** フラグによる画面表示制御は、DELETE APIでの認可チェックを代替しない。

## 5. 実装予定範囲

- `recipes.ts` にDELETE API関数を追加
- `RecipeEditPage.tsx` に削除操作を追加
- 削除確認・キャンセルを実装
- 処理中の二重送信防止
- 削除失敗時のエラー表示
- 削除成功時の一覧画面への遷移
- 必要に応じてBackendとFrontendのHTTPエラー仕様を調整

## 6. 実装前・テスト時の確認事項

- 作成者は削除できるか
- 他人のレシピは削除できないか
- 未認証の場合は拒否されるか
- 存在しないIDは適切に処理されるか
- 削除済みIDの再削除は拒否されるか
- 削除後、詳細APIから取得できなくなるか
- 削除後、一覧APIに表示されなくなるか
- 確認画面でキャンセルした場合は削除されないか
- 削除中に更新・削除を再実行できないか
- 他人が編集画面を直接開いた場合の操作表示とAPI拒否が適切か
- FrontendのビルドとBackendのテストが成功するか

## 7. 今回の調査で得た学び・Evidence

### 実施内容

既存のFrontend・Backendコードを `grep`、`cat`、`sed` で調査し、画面、API、Service、DTOのつながりを確認した。

Git履歴から、`RecipeDetail.ts` がPR #26で追加されていたことを確認した。

### 発見した課題

- 削除機能のBackend処理は既存だが、Frontend操作は未実装
- 閲覧権限と編集・削除権限の違い
- UPDATEとDELETEの所有者チェックの重複
- APIのHTTPステータスとFrontendの期待値の不整合
- 更新と削除の同時実行リスク

### 設計上の判断

新規画面を作るのではなく、既存の編集画面を活用する。

将来の権限拡張を考慮しつつ、現時点で不要なフラグや共通化は実装せず、設計候補として記録する。

### 次に実施すること

1. DELETE API関数の実装
2. 編集画面への削除操作の追加
3. API・画面テスト
4. 調査結果と実装結果の差分を記録
5. PR作成とレビュー

---

**記録時点：実装前の調査・設計段階。未実施のテストを完了扱いにしない。**