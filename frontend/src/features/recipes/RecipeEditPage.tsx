import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getRecipeDetail } from "../../api/recipes";
import type { UpdateRecipeRequest } from "../../types/UpdateRecipeRequest";
import BackButton from "../../components/ui/BackButton";
import LoadingSpinner from "../../components/ui/LoadingSpinner";

export default function RecipeEditPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const recipeId = Number(id);
  const isValidId = !!id && Number.isSafeInteger(recipeId) && recipeId > 0;

  const [form, setForm] = useState<UpdateRecipeRequest>({
    name: "",
    description: "",
    cookedDate: "",
    visibility: "FAMILY",
  });

  const [isLoading, setIsLoading] = useState(true);
  const [errMessage, setErrMessage] = useState<string | null>(null);

  useEffect(() => {
    const recipeId = Number(id);

    if (!isValidId) {
      return;
    }

    let cancelled = false;

    async function loadRecipe() {
      setIsLoading(true);
      setErrMessage(null);

      try {
        const recipe = await getRecipeDetail(recipeId);

        if (cancelled) return;

        setForm({
          name: recipe.name,
          description: recipe.description ?? "",
          cookedDate: recipe.cookedDate ?? "",
          visibility: recipe.visibility,
        });
      } catch (error) {
        if (cancelled) return;

        setErrMessage(
          error instanceof Error ? error.message : "レシピの取得に失敗しました",
        );
      } finally {
        if (!cancelled) setIsLoading(false);
      }
    }

    void loadRecipe();

    return () => {
      cancelled = true;
    };
  }, [id, isValidId]);

  if (!isValidId) {
    return (
      <main className="mx-auto max-w-md px-4 py-6">
        <BackButton />
        <p role="alert" className="text-red-600">
          レシピIDが不正です
        </p>
      </main>
    );
  }

  if (isLoading) {
    return (
      <main className="mx-auto max-w-md px-4 py-6">
        <LoadingSpinner />
        <p>レシピを読み込み中...</p>
      </main>
    );
  }

  if (errMessage) {
    return (
      <main className="mx-auto max-w-md px-4 py-6">
        <BackButton />
        <p role="alert" className="text-red-600">
          {errMessage}
        </p>
      </main>
    );
  }

  return (
    <main className="mx-auto max-w-md px-4 py-6">
      <BackButton />
      <h1 className="m-4 text-2xl font-bold">レシピを編集する</h1>

      <form>
        <label htmlFor="name">料理名</label>
        <input
          id="name"
          type="text"
          required
          value={form.name}
          onChange={(e) => setForm({ ...form, name: e.target.value })}
          className="mb-3 w-full rounded-lg border p-3"
        />

        <label htmlFor="description">料理の説明</label>
        <textarea
          id="description"
          value={form.description}
          onChange={(e) => setForm({ ...form, description: e.target.value })}
          className="mb-3 w-full rounded-lg border p-3"
        />

        <label htmlFor="cookedDate">調理日</label>
        <input
          id="cookedDate"
          type="date"
          required
          value={form.cookedDate}
          onChange={(e) => setForm({ ...form, cookedDate: e.target.value })}
          className="mb-3 w-full rounded-lg border p-3"
        />

        <fieldset className="my-4">
          <legend className="font-bold">公開範囲</legend>
          {(["FAMILY", "PRIVATE", "PUBLIC"] as const).map((value) => (
            <label key={value} className="block p-2">
              <input
                type="radio"
                name="visibility"
                value={value}
                checked={form.visibility === value}
                onChange={() => setForm({ ...form, visibility: value })}
                className="mr-2"
              />
              {value === "FAMILY"
                ? "家族のみ"
                : value === "PRIVATE"
                  ? "自分のみ"
                  : "公開"}
            </label>
          ))}
        </fieldset>

        <button type="button" onClick={() => navigate(`/recipes/${id}`)}>
          詳細画面に戻る
        </button>
      </form>
    </main>
  );
}
