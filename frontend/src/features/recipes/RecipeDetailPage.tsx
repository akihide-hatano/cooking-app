import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getRecipeDetail } from "../../api/recipes";
import type { RecipeDetail } from "../../types/RecipeDetail";

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
    <div>
      <h1>Recipe Detail Page</h1>
      <p>Recipe ID: {id}</p>
      <p>Recipe Name: {recipe?.name ?? "読み込み中"}</p>
      <p>Recipe Description: {recipe?.description ?? "読み込み中"}</p>
      <p>Cooked Date: {recipe?.cookedDate ?? "未登録"}</p>
      <p>Visibility: {recipe?.visibility ?? "読み込み中"}</p>
      <p>Image URL: {recipe?.imageUrl ?? "読み込み中"}</p>
      <h2>Steps</h2>
      <ul>
        {recipe?.steps.map((step) => (
          <li key={step.id}>
            {step.sortOrder}. {step.description}
          </li>
        )) ?? "読み込み中"}
      </ul>
    </div>
  );
}
