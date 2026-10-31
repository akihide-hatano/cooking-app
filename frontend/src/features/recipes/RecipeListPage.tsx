import { Link } from "react-router-dom";
import type { Recipe } from "../../types/Recipe";
import RecipeCard from "./components/RecipeCard";
import { getRecipes } from "../../api/recipes";
import { useEffect, useState } from "react";

export default function RecipeListPage() {
  const [recipes, setRecipes] = useState<Recipe[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadRecipes = async () => {
      try {
        const data = await getRecipes();
        setRecipes(data);
      } catch (error) {
        console.error("レシピの取得に失敗しました:", error);
        setError("レシピの取得に失敗しました");
      } finally {
        setLoading(false);
      }
    };
    void loadRecipes();
  }, []);

  return (
    <main className="min-h-screen bg-cream px-4 py-6 md:px-8">
      <div className="mx-auto max-w-6xl">
        <div className="flex justify-between items-center mb-4">
          <h1 className="text-2xl font-bold text-text-main md:text-3xl">
            我が家のレシピ
          </h1>
          <div className="flex items-baseline">
            <span className="text-2xl">{recipes.length}</span>
            <span className="text-xs">品</span>
          </div>
        </div>
        <div className="mb-6 flex w-full justify-center lg:justify-end">
          <Link
            to="/recipes/new"
            className="
      flex h-13 w-full items-center justify-center
      rounded-button bg-brand px-6
      font-bold text-white shadow-sm
      transition-colors hover:bg-brand-dark
      lg:w-auto
    "
          >
            ＋ 料理を追加する
          </Link>
        </div>
        {loading ? (
          <div className="rounded-card bg-white px-6 py-16 text-center">
            <p>レシピを読み込み中...</p>
          </div>
        ) : error ? (
          <div className="rounded-card bg-white px-6 py-16 text-center">
            <p>{error}</p>
          </div>
        ) : recipes.length === 0 ? (
          <div className="rounded-card bg-white px-6 py-16 text-center">
            <p className="mb-2 text-sm font-bold text-text-main">
              レシピがありません
            </p>
            <p className="text-sm text-text-muted">
              まずはレシピを追加してみましょう
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-2 gap-4 lg:grid-cols-3">
            {recipes.map((recipe) => (
              <RecipeCard key={recipe.id} recipe={recipe} />
            ))}
          </div>
        )}
      </div>
    </main>
  );
}
