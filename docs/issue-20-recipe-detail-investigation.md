# Issue #20 調査結果：レシピ詳細取得API・JWT認証・React連携

調査日：2026-10-25  
対象：料理投稿アプリ（React / TypeScript / Spring Boot / PostgreSQL）  
対象Issue：#20 レシピ詳細取得・表示

## 1. 調査の目的

レシピ詳細画面を実装する前に、既存バックエンドのAPI仕様、認証・認可、例外処理、フロントエンドとの接続方法を確認する。

今回の調査では、既存コードを大きく変更することではなく、**現在の処理を理解し、フロントエンド実装に必要な情報を明確にすること**を目的とした。

## 2. 調査結果の要約

- レシピ詳細取得API `GET /api/recipes/{id}` は実装済み。
- レシピ本体と調理手順をまとめて取得できる。
- `PRIVATE`、`FAMILY`、`PUBLIC` の閲覧制御は `RecipeService` で実装されている。
- 削除済みレシピ・削除済み調理手順は取得対象から除外される。
- 調理手順は `sortOrder` の昇順で取得する。
- JWTの発行・署名検証・有効期限設定が実装されている。
- 存在しないレシピと閲覧権限のないレシピは404として扱われる。
- React側の詳細画面はAPI未接続であり、これから実装する。
- 無効・期限切れJWTのHTTPエラー処理には確認・改善の余地がある。

## 3. バックエンドの処理構成

### 3.1 API

| 項目 | 内容 |
|---|---|
| HTTPメソッド | GET |
| エンドポイント | `/api/recipes/{id}` |
| 認証 | JWT Bearer認証 |
| 成功時 | HTTP 200 |
| レシピ不存在 | HTTP 404 |
| 閲覧権限なし | HTTP 404 |

### 3.2 処理の流れ

1. ReactからJWT付きで `GET /api/recipes/{id}` を送信する。
2. `JwtAuthenticationFilter` がJWTを検証し、ユーザーIDを認証情報に設定する。
3. `SecurityConfig` のルールにより、認証が必要なAPIへのアクセスを制御する。
4. `RecipeController` がURLのレシピIDを受け取る。
5. `RecipeService.getRecipeDetail()` が処理を開始する。
6. `getRecipe()` でレシピの存在、論理削除状態、閲覧権限を確認する。
7. `RecipeStepRepository` で削除されていない調理手順を取得する。
8. `RecipeDetailData` にレシピと調理手順をまとめる。
9. Controllerが `RecipeDetailResponse` に変換し、JSONを返す。
10. Reactがレスポンスを受け取り、画面に表示する。

※10は今後のフロントエンド実装で対応する。

### 3.3 各クラスの役割

| クラス | 役割 |
|---|---|
| `RecipeController` | HTTPリクエスト受付・レスポンス生成 |
| `RecipeService` | レシピ取得・閲覧権限判定 |
| `RecipeRepository` | レシピデータ取得 |
| `RecipeStepRepository` | 調理手順取得 |
| `RecipeDetailData` | ServiceからControllerへ渡す内部データ |
| `RecipeDetailResponse` | Reactに返す詳細レスポンスDTO |
| `RecipeStepResponse` | 調理手順のレスポンスDTO |
| `JwtService` | JWT発行・検証 |
| `JwtAuthenticationFilter` | JWTから認証情報を設定 |
| `SecurityConfig` | APIの認証ルール設定 |
| `GlobalExceptionHandler` | 例外をHTTPレスポンスへ変換 |

## 4. レシピ詳細APIのデータ構造

`RecipeDetailResponse` の項目は以下のとおり。

| 項目 | Javaの型 | TypeScriptで想定する型 |
|---|---|---|
| id | Long | number |
| name | String | string |
| description | String | string |
| cookedDate | LocalDate | string または null（実際のnull許容は要確認） |
| visibility | RecipeVisibility | `"PRIVATE" \| "FAMILY" \| "PUBLIC"` |
| imageUrl | String | string または null |
| steps | List&lt;RecipeStepResponse&gt; | RecipeStep[] |

調理手順の項目：

| 項目 | 型 |
|---|---|
| id | number |
| description | string |
| sortOrder | number |

レスポンスのイメージ（説明用の例）：

```json
{
  "id": 4,
  "name": "肉じゃが",
  "description": "家族の定番料理",
  "cookedDate": "2025-02-16",
  "visibility": "FAMILY",
  "imageUrl": null,
  "steps": [
    {
      "id": 10,
      "description": "材料を切る",
      "sortOrder": 1
    },
    {
      "id": 11,
      "description": "鍋で煮込む",
      "sortOrder": 2
    }
  ]
}
```

### DTOについて理解したこと

- `GetRecipeResponse` はレシピ一覧用であり、調理手順を含まない。
- `RecipeDetailResponse` はレシピ詳細用であり、画像URLと調理手順を含む。
- `RecipeDetailData` はServiceとControllerの間で使用する内部データであり、Reactに直接返すDTOではない。
- Controllerの `stream().map().toList()` はデータ変換を行うが、並び替えを行うわけではない。
- 調理手順の並び順はRepositoryの `OrderBySortOrderAsc` により指定している。
- 現在の詳細レスポンスには家族名が含まれていないため、デザイン上の家族名表示には別途検討が必要。

## 5. 認証と認可の違い

### 認証（Authentication）

「誰がアクセスしているか」を確認すること。

今回のアプリではJWTを利用する。

### 認可（Authorization）

「そのユーザーが対象データを閲覧してよいか」を判断すること。

今回のアプリでは `RecipeService.getRecipe()` がレシピ単位の閲覧権限を確認する。

### 閲覧権限

| 公開範囲 | 閲覧条件 |
|---|---|
| PRIVATE | 作成者本人のみ |
| FAMILY | 対象レシピの家族に現在所属しているユーザー |
| PUBLIC | 認証済みユーザーであれば閲覧可能 |

※PUBLICを未ログインユーザーにも公開するかどうかは、現在の認証設定とは別の仕様判断となる。

家族所属の判定では、論理削除された所属情報や削除済み家族を除外するRepositoryメソッドを使用している。

また、閲覧権限がない場合も404を返すことで、レシピの存在情報を不用意に公開しない設計になっている。

## 6. JWTの仕組み

### 6.1 JWT発行

`JwtService.generateToken(userId)` がJWTを発行する。

JWTには以下の情報を設定する。

- subject：ユーザーID
- issuedAt：発行日時
- expiration：有効期限
- 署名：`JWT_SECRET` を元に生成した鍵で署名

有効期限は24時間。

### 6.2 JWT検証

`JwtService.extractUserId(token)` は署名付きJWTを解析・検証し、subjectからユーザーIDを取得する。

`JwtAuthenticationFilter` はHTTPの `Authorization: Bearer ...` ヘッダーを読み取り、ユーザーIDを `SecurityContextHolder` に登録する。

`RecipeService` はこの認証情報からユーザーIDを取得する。

### 6.3 環境変数との関係

`JWT_SECRET` はJWTの署名・検証に使う秘密情報である。

Docker Composeやデプロイ環境で `JWT_SECRET` を正しく設定する必要がある理由を、コード上で確認できた。

## 7. 例外処理とHTTPステータス

`GlobalExceptionHandler` に以下の処理が実装されている。

| 例外 | HTTPステータス |
|---|---|
| IllegalArgumentException | 400 |
| MethodArgumentNotValidException | 400 |
| RecipeNotFoundException | 404 |

`RecipeNotFoundException` は、レシピが存在しない場合だけでなく、閲覧権限がない場合にも使用される。

### 今後の確認事項

- JWTなしの場合に401または403のどちらが返るか。
- JWT期限切れ時にどのHTTPステータスが返るか。
- 不正な署名や壊れたJWTで想定外の500にならないか。
- React側で401・403・404・通信エラーをどう扱うか。

注意：JWTの解析はControllerより前のFilterで実行される。`GlobalExceptionHandler` だけでFilter内の例外を処理できるとは限らないため、認証エラーの扱いはSpring Security側の設定も含めて検討する。

## 8. フロントエンドの調査結果

確認したファイル：

- `frontend/src/App.tsx`
- `frontend/src/api/recipes.ts`
- `frontend/src/types/Recipe.ts`
- `frontend/src/features/recipes/RecipeDetailPage.tsx`
- `frontend/src/utils/tokenStorage.ts`（既存APIコードから利用を確認）

### 現状

- `/recipes/:id` のルーティングは設定済み。
- `RecipeDetailPage.tsx` はURLのIDを表示する仮画面。
- `recipes.ts` にはレシピ作成API関数が実装されている。
- 作成APIではJWTを `Authorization` ヘッダーに付与している。
- `Recipe` 型には詳細APIの `steps` が含まれていない。
- 詳細取得APIを呼ぶフロントエンド関数は未実装。

### 今後の実装

1. 詳細レスポンスのTypeScript型を定義する。
2. `getRecipeById(id)` を実装する。
3. JWTを付けて詳細APIを呼ぶ。
4. `RecipeDetailPage` で取得結果を状態管理する。
5. レシピ名・説明・調理日・公開範囲・手順を表示する。
6. 読み込み中・未認証・404・通信エラーの表示を実装する。
7. 正常系・異常系のテストと画面確認を行う。

画像アップロード、レシピ編集、手順追加などは今回のIssueの対象外とする。

## 9. 前回までの開発で得た知見

### バックエンドとフロントエンドは別々に完成しても、機能全体の完成とは限らない

Issue #20ではバックエンド実装をPR #25でmainへマージしたが、フロントエンド表示が未完了だったためIssueを再オープンした。

今後は、**Issueの完了条件を満たしたかを基準にクローズする**。

### Docker・DB・JWTの設定は実装とつながっている

過去の動作確認では、Docker ComposeのDB接続設定やJWT秘密鍵の設定を確認した。

今回のコード調査によって、環境変数が実際のSpring Bootのどの処理で使用されているかを理解できた。

### 調査を広げすぎない

調査自体は必要だが、調査の終了条件がないと、周辺技術の確認が続いて実装が進まなくなる。

今後は以下のように判断する。

- 今のIssueの完成に必要：調査・対応する
- 重要だが今回の完了条件には直接関係しない：別Issueや調査メモに残す
- 実装後でも確認できる：まず動く状態を作り、その後検証する

## 10. 今回の学習で理解したこと

- Controller、Service、Repository、DTOには異なる責務がある。
- JWT認証とレシピ単位の認可は別の処理である。
- JavaのDTOとTypeScriptの型はAPI契約によってつながっている。
- Repositoryの検索条件は、画面に表示されるデータの内容や順番に影響する。
- 論理削除では、削除日時を持つデータを検索対象から除外する必要がある。
- HTTPステータスはReact側の画面表示・エラー表示の判断材料になる。
- ライブラリのメソッドを暗記するより、処理の目的とデータの流れを説明できることが重要である。

## 11. 今後の学習・開発方針

目指す順序：

**コードを読める → 小さく変更できる → 変更による動作の違いを説明できる → 自分で設計判断できる**

AIの活用方法：

1. 自分で要件・完了条件を確認する。
2. AIに実装案やコードを提案してもらう。
3. 自分で処理を読み、理由を説明する。
4. テストで実際の動作を確認する。
5. 別のAIにもレビューさせ、指摘の根拠を確認する。
6. 最終的な品質判断は自分で行う。

## 12. 次のアクション

**Issue #20のフロントエンド実装を完成させる。**

最初に現在のGit状態とフロントエンドの既存コードを確認する。

その後、API型定義 → API取得関数 → 画面表示 → 異常系 → テスト → PRの順番で進める。

JWTの異常系改善など、今回の実装に直接必要でない変更は別課題として管理する。

---

**今回の結論**

バックエンドのレシピ詳細取得処理は概ね整っており、フロントエンド実装へ進むためのAPI契約と主要な認証・認可の仕組みを確認できた。

今後は調査の範囲を適切に制御し、理解を深めながら小さく機能を完成させる。
