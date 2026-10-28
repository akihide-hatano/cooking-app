import { useEffect, useState, type SubmitEvent } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getRecipeDetail, updateRecipe } from "../../api/recipes";
import type { UpdateRecipeRequest } from "../../types/UpdateRecipeRequest";
import BackButton from "../../components/ui/BackButton";
import LoadingSpinner from "../../components/ui/LoadingSpinner";
import PrimaryButton from "../../components/ui/PrimaryButton";

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

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [saveError, setSaveError] = useState<string | null>(null);

  async function handleSubmit(e: SubmitEvent<HTMLFormElement>) {
    e.preventDefault();

    if (!isValidId || isSubmitting || isLoading) {
      return;
    }

    setIsSubmitting(true);
    setSaveError(null);

    try {
      await updateRecipe(recipeId, form);
      navigate(`/recipes/${recipeId}`);
    } catch (error) {
      setSaveError(
        error instanceof Error ? error.message : "レシピの更新に失敗しました",
      );
    } finally {
      setIsSubmitting(false);
    }
  }

  useEffect(() => {
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
  }, [recipeId, isValidId]);

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

      <form onSubmit={handleSubmit}>
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
          <legend className="mb-2 font-bold">公開範囲</legend>

          <div className="space-y-2">
            {(["FAMILY", "PRIVATE", "PUBLIC"] as const).map((value) => (
              <label
                key={value}
                className={`flex cursor-pointer items-center gap-3 rounded-lg border-2 p-3 ${
                  form.visibility === value
                    ? "border-brand bg-brand/20 font-bold"
                    : "border-gray-300 bg-white"
                }`}
              >
                <input
                  type="radio"
                  name="visibility"
                  value={value}
                  checked={form.visibility === value}
                  onChange={() =>
                    setForm((prev) => ({ ...prev, visibility: value }))
                  }
                  className="accent-brand"
                />

                {value === "FAMILY"
                  ? "家族のみ"
                  : value === "PRIVATE"
                    ? "自分のみ"
                    : "公開"}
              </label>
            ))}
          </div>
        </fieldset>

        {saveError && (
          <p role="alert" className="text-red-600">
            {saveError}
          </p>
        )}

        <PrimaryButton type="submit" disabled={isSubmitting} className="mt-4">
          {isSubmitting ? "保存中..." : "変更を保存する"}
        </PrimaryButton>
      </form>
    </main>
  );
}
