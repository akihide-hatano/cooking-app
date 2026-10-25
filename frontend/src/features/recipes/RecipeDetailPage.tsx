import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getRecipeDetail } from "../../api/recipes";
import type { RecipeDetail } from "../../types/RecipeDetail";
import { RECIPE_VISIBILITY_LABELS } from "../../types/RecipeVisibility";

export default function RecipeDetailPage() {
  const { id } = useParams<{ id: string }>();
  const [recipe, setRecipe] = useState<RecipeDetail | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (id) {
      getRecipeDetail(Number(id))
        .then(setRecipe)
        .catch((error) => {
          console.error(error);
          setError("レシピの取得に失敗しました");
        });
    }
  }, [id]);

  if (error) {
    return <p>{error}</p>;
  }

  if (!recipe) {
    return <p>読み込み中...</p>;
  }

  return (
    <main className="min-h-screen bg-cream px-4 py-8">
      <div className="mx-auto max-w-2xl">
        <h1 className="mb-6 text-2xl font-bold text-brand">料理の詳細</h1>

        <section className="rounded-card border border-border-soft bg-surface p-6">
          <h2 className="text-xl font-bold text-text-main">
            {recipe?.name ?? "読み込み中"}
          </h2>

          <p className="mt-3 text-text-muted">{recipe?.description ?? ""}</p>

          <div className="mt-6 border-t border-border-soft pt-4">
            <p className="text-sm text-text-muted">
              調理日：{recipe?.cookedDate ?? "未登録"}
            </p>

            <p className="mt-2 text-sm text-text-muted">
              公開範囲：{RECIPE_VISIBILITY_LABELS[recipe.visibility]}
            </p>
          </div>
        </section>

        <section className="mt-8">
          <div className="mb-5 flex items-end justify-between">
            <div>
              <p className="mb-1 text-sm font-bold text-brand">作り方</p>
              <h2 className="text-2xl font-bold text-text-main">調理手順</h2>
            </div>

            <span className="text-sm text-text-muted">
              {recipe?.steps.length ?? 0}手順
            </span>
          </div>

          <ol className="space-y-3">
            {recipe?.steps.map((step) => (
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
        </section>
      </div>
    </main>
  );
}
