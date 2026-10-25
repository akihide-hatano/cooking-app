import ActionButton from "../../components/ui/ActionButton";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getRecipeDetail } from "../../api/recipes";
import type { RecipeDetail } from "../../types/RecipeDetail";
import { RECIPE_VISIBILITY_LABELS } from "../../types/RecipeVisibility";
import PrimaryButton from "../../components/ui/PrimaryButton";
import { useNavigate } from "react-router-dom";
import LoadingSpinner from "../../components/ui/LoadingSpinner";
import RecipeVisibilityBadge from "../../components/ui/RecipeVisibilityBadge";

export default function RecipeDetailPage() {
  const { id } = useParams<{ id: string }>();
  const [recipe, setRecipe] = useState<RecipeDetail | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [newStep, setNewStep] = useState("");
  const [isAddingStep, setIsAddingStep] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    let active = true;
    if (id) {
      getRecipeDetail(Number(id))
        .then((data) => {
          if (active) {
            setRecipe(data);
          }
        })
        .catch((error: unknown) => {
          if (!active) return;
          console.error(error);
          setError(
            error instanceof Error
              ? error.message
              : "レシピの取得に失敗しました",
          );
        });
    }
    return () => {
      active = false;
    };
  }, [id]);

  if (error) {
    return (
      <main className="flex min-h-screen items-center justify-center bg-cream px-4">
        <div className="w-full max-w-md rounded-card border border-border-soft bg-surface p-8 text-center">
          <h1 className="text-xl font-bold text-text-main">
            レシピを表示できません
          </h1>

          <p role="alert" className="mt-4 text-text-muted">
            {error}
          </p>

          <div className="mt-6">
            <PrimaryButton onClick={() => navigate("/recipes")}>
              レシピ一覧に戻る
            </PrimaryButton>
          </div>
        </div>
      </main>
    );
  }

  if (!recipe) {
    return (
      <div
        role="status"
        aria-label="料理を読み込み中"
        className="flex min-h-screen items-center justify-center bg-cream"
      >
        <LoadingSpinner />
      </div>
    );
  }
  return (
    <main className="min-h-screen bg-cream px-4 py-8">
      <div className="mx-auto max-w-2xl">
        <h1 className="mb-6 text-2xl font-bold text-brand">料理の詳細</h1>

        <section className="rounded-card border border-border-soft bg-surface p-6">
          <div className="my-2">
            <RecipeVisibilityBadge visibility={recipe.visibility} />
          </div>
          <h2 className="text-xl font-bold text-text-main">{recipe.name}</h2>

          <p className="mt-3 text-text-muted">{recipe.description ?? ""}</p>

          <div className="mt-6 grid grid-cols-2 gap-4 border-t border-border-soft pt-5">
            <div>
              <p className="text-sm text-text-muted">調理日</p>
              <p className="mt-2 font-semibold text-text-main">
                {recipe.cookedDate
                  ? recipe.cookedDate.replaceAll("-", "/")
                  : "未登録"}
              </p>
            </div>

            <div>
              <p className="text-sm text-text-muted">公開範囲</p>
              <p className="mt-2 font-semibold text-text-main">
                {recipe
                  ? RECIPE_VISIBILITY_LABELS[recipe.visibility]
                  : "未設定"}
              </p>
            </div>
          </div>

          <div className="mt-6">
            <PrimaryButton
              onClick={() => navigate(`/recipes/${recipe.id}/edit`)}
            >
              料理を編集する
            </PrimaryButton>
          </div>
        </section>

        <section className="mt-8">
          <div className="mb-5 flex items-end justify-between">
            <div>
              <p className="mb-1 text-sm font-bold text-brand">作り方</p>
              <h2 className="text-2xl font-bold text-text-main">調理手順</h2>
            </div>

            <span className="text-sm text-text-muted">
              {recipe.steps.length}手順
            </span>
          </div>

          <ol className="space-y-3">
            {recipe.steps.map((step) => (
              <li
                key={step.id}
                className="flex items-center gap-4 rounded-card border border-border-soft bg-surface p-5"
              >
                <span className="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-brand text-lg font-bold text-white">
                  {step.sortOrder}
                </span>

                <p className="text-base text-text-main">{step.description}</p>
              </li>
            ))}
          </ol>
          {isAddingStep && (
            <div className="mt-4">
              <input
                type="text"
                value={newStep}
                onChange={(e) => setNewStep(e.target.value)}
                className="w-full rounded border border-border-soft p-2"
                placeholder="新しい手順を入力"
              />
              <div className="mt-2 flex justify-end gap-2">
                <ActionButton
                  disabled={!newStep}
                  onClick={() => {
                    // ここで新しい手順を追加する処理を実装
                    setIsAddingStep(false);
                    setNewStep("");
                  }}
                >
                  追加
                </ActionButton>
                <button
                  type="button"
                  onClick={() => {
                    setIsAddingStep(false);
                    setNewStep("");
                  }}

                  className="
                              flex h-13 w-full items-center justify-center
                              rounded-button border border-border-soft
                              bg-white px-6 font-bold text-text-main
                              transition-colors hover:bg-cream
                            "
                >
                  キャンセル
                </button>
              </div>
            </div>
          )}
          {!isAddingStep && (
            <div className="mt-4">
              <ActionButton onClick={() => setIsAddingStep(true)}>
                手順を追加
              </ActionButton>
            </div>
          )}
        </section>
      </div>
    </main>
  );
}
