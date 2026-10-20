import LoadingSpinner from "../../components/ui/LoadingSpinner";
import type { Recipe } from "../../types/Recipe";
import { createRecipe } from "../../api/recipes";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import BackButton from "../../components/ui/BackButton";
import PrimaryButton from "../../components/ui/PrimaryButton";
import type { SubmitEvent } from "react";

export default function RecipeCreatePage() {
  const navigate = useNavigate();
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [cookDate, setCookDate] = useState("");
  const [visibility, setVisibility] = useState<Recipe["visibility"]>("FAMILY");
  const [imagePreviewUrl, setImagePreviewUrl] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errMessage, setErrMessage] = useState<string | null>(null);

  useEffect(() => {
    return () => {
      if (imagePreviewUrl) {
        URL.revokeObjectURL(imagePreviewUrl);
      }
    };
  }, [imagePreviewUrl]);

  async function handleSubmit(e: SubmitEvent<HTMLFormElement>) {
    e.preventDefault();
    if (isSubmitting) return;
    setIsSubmitting(true);
    setErrMessage(null);
    try {
      const recipeData = {
        name,
        description,
        cookedDate: cookDate,
        visibility,
      };
      const createdRecipe = await createRecipe(recipeData);
      console.log("送信データ", recipeData);
      console.log("作成されたレシピ", createdRecipe);
      navigate(`/recipes/${createdRecipe.id}`);
    } catch (error) {
      console.error("レシピの作成に失敗しました", error);
      if (error instanceof Error) {
        setErrMessage(error.message);
      } else {
        setErrMessage("レシピの作成に失敗しました");
      }
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <main className="mx-auto max-w-md px-4 py-6">
      <BackButton />
      <h1 className="text-2xl font-bold m-4">レシピを登録する</h1>
      {errMessage && (
        <p
          role="alert"
          className="rounded bg-red-100 px-2 py-1 text-xs font-bold text-red-600"
        >
          {errMessage}
        </p>
      )}

      <form onSubmit={handleSubmit}>
        <div className="flex items-center gap-3 mb-2">
          <label htmlFor="name" className="block font-bold">
            料理名
          </label>
          <span className="rounded bg-red-100 px-2 py-1 text-xs font-bold text-red-600">
            必須
          </span>
        </div>
        <input
          id="name"
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="料理名を入力してください"
          className="w-full rounded-lg border border-green-700 bg-white p-3 mb-2"
          required
        />

        <label htmlFor="description" className="block mb-2 font-bold">
          料理の説明
        </label>
        <textarea
          id="description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="料理の説明を入力してください"
          className="w-full rounded-lg border border-green-700 bg-white p-3 mb-2"
        ></textarea>

        <div className="flex items-center gap-3 mb-2">
          <label htmlFor="cookDate" className="block  font-bold">
            調理日
          </label>
          <span className="rounded bg-red-100 px-2 py-1 text-xs font-bold text-red-600">
            必須
          </span>
        </div>
        <input
          id="cookDate"
          type="date"
          value={cookDate}
          onChange={(e) => setCookDate(e.target.value)}
          className="w-full rounded-lg border border-green-700 bg-white p-3 mb-2"
          required
        />

        <fieldset className="my-4">
          <legend className="mb-2 font-bold">
            <span className="inline-flex items-center gap-3">
              <span>公開範囲</span>
              <span className="rounded bg-red-100 px-2 py-1 text-xs font-bold text-red-600">
                必須
              </span>
            </span>
          </legend>
          <p id="visibility-description" className="mb-2 text-sm text-gray-500">
            この料理を見られる人を選びます
          </p>
          <div className="space-y-2">
            <div
              className={`flex items-center gap-3 rounded-lg border p-3 ${
                visibility === "FAMILY"
                  ? "border-green-700 bg-green-50"
                  : "border-gray-300 bg-white"
              }`}
            >
              <input
                id="visibility-family"
                type="radio"
                name="visibility"
                value="FAMILY"
                checked={visibility === "FAMILY"}
                onChange={(e) =>
                  setVisibility(e.target.value as Recipe["visibility"])
                }
                className="accent-green-800"
              />
              <label htmlFor="visibility-family">家族のみ</label>
            </div>
            <div
              className={`flex items-center gap-3 rounded-lg border p-3 ${
                visibility === "PRIVATE"
                  ? "border-green-700 bg-green-50"
                  : "border-gray-300 bg-white"
              }`}
            >
              <input
                id="visibility-private"
                type="radio"
                name="visibility"
                value="PRIVATE"
                checked={visibility === "PRIVATE"}
                onChange={(e) =>
                  setVisibility(e.target.value as Recipe["visibility"])
                }
                className="accent-green-800"
              />
              <label htmlFor="visibility-private">自分のみ</label>
            </div>
            <div
              className={`flex items-center gap-3 rounded-lg border p-3 ${
                visibility === "PUBLIC"
                  ? "border-green-700 bg-green-50"
                  : "border-gray-300 bg-white"
              }`}
            >
              <input
                id="visibility-public"
                type="radio"
                name="visibility"
                value="PUBLIC"
                checked={visibility === "PUBLIC"}
                onChange={(e) =>
                  setVisibility(e.target.value as Recipe["visibility"])
                }
                className="accent-green-800"
              />
              <label htmlFor="visibility-public">公開</label>
            </div>
          </div>
        </fieldset>

        <label htmlFor="imageFile" className="block mb-2 font-bold">
          料理の画像
        </label>
        <input
          id="imageFile"
          type="file"
          accept="image/*"
          onChange={(e) => {
            const file = e.target.files?.[0] ?? null;
            if (file) {
              setImagePreviewUrl(URL.createObjectURL(file));
            } else {
              setImagePreviewUrl(null);
            }
          }}
          className="w-full rounded-lg border border-green-700 bg-white p-3 mb-2"
        />
        {imagePreviewUrl && (
          <div className="mt-3 mb-3">
            <p>選択された画像:</p>
            <img
              src={imagePreviewUrl}
              alt="選択された料理の画像"
              className="w-full rounded-lg border border-green-700 bg-white p-3"
            />
          </div>
        )}

        <PrimaryButton type="submit" disabled={isSubmitting}>
          {isSubmitting ? (
            <>
              <LoadingSpinner />
              <span className="ml-2">登録中...</span>
            </>
          ) : (
            "料理を登録する"
          )}
        </PrimaryButton>
      </form>
    </main>
  );
}
