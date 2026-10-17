import type { Recipe } from "../../types/Recipe";
import { useEffect, useState } from "react";
import BackButton from "../../components/ui/BackButton";
// import { useNavigate } from "react-router-dom";

export default function RecipeCreatePage() {
  //   const navigate = useNavigate();
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [cookDate, setCookDate] = useState("");
  const [visibility, setVisibility] = useState<Recipe["visibility"]>("FAMILY");
  const [imagePreviewUrl, setImagePreviewUrl] = useState<string | null>(null);

  useEffect(() => {
    return () => {
      if (imagePreviewUrl) {
        URL.revokeObjectURL(imagePreviewUrl);
      }
    };
  }, [imagePreviewUrl]);

  return (
    <main className="mx-auto max-w-md px-4 py-6">
      <BackButton />
      <h1 className="text-2xl font-bold m-4">レシピを登録する</h1>

      <label htmlFor="name" className="block mb-2 font-bold">
        料理名
      </label>
      <input
        id="name"
        type="text"
        value={name}
        onChange={(e) => setName(e.target.value)}
        placeholder="料理名を入力してください"
        className="w-full rounded-lg border-2 border-gray-500 bg-white p-3 mb-2"
      />

      <label htmlFor="description" className="block mb-2 font-bold">
        料理の説明
      </label>
      <textarea
        id="description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        placeholder="料理の説明を入力してください"
        className="w-full rounded-lg border-2 border-gray-500 bg-white p-3 mb-2"
      ></textarea>

      <label htmlFor="cookDate" className="block mb-2 font-bold">
        調理日
      </label>
      <input
        id="cookDate"
        type="date"
        value={cookDate}
        onChange={(e) => setCookDate(e.target.value)}
        className="w-full rounded-lg border-2 border-gray-500 bg-white p-3 mb-2"
      />

      <div className="my-4">
        <div className="flex items-center gap-3 mb-2">
          <p className="font-bold">公開範囲</p>
          <span className="rounded bg-red-100 px-2 py-1 text-xs font-bold text-red-600">
            必須
          </span>
        </div>
        <p className="mb-2 text-sm text-gray-500">
          この料理を見られる人を選びます
        </p>
        <div className="space-y-2">
          <div className="flex items-center gap-3 rounded-lg border border-gray-300 p-3">
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
          <div className="flex items-center gap-3 rounded-lg border border-gray-300 p-3">
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
          <div className="flex items-center gap-3 rounded-lg border border-gray-300 p-3">
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
      </div>

      <label htmlFor="imageFile" className="block mb-2 font-bold">
        料理の画像
      </label>
      <input
        id="imageFile"
        type="file"
        accept="image/*"
        onChange={(e) => {
          console.log("target", e.target);
          console.log("files", e.target.files);
          console.log("imageFile", e.target.files ? e.target.files[0] : null);
          const file = e.target.files?.[0] ?? null;
          if (file) {
            setImagePreviewUrl(URL.createObjectURL(file));
          } else {
            setImagePreviewUrl(null);
          }
        }}
        className="w-full rounded-lg border-2 border-gray-500 bg-white p-3 mb-2"
      />
      {imagePreviewUrl && (
        <div className="mt-3">
          <p>選択された画像:</p>
          <img
            src={imagePreviewUrl}
            alt="選択された料理の画像"
            className="w-full rounded-lg border-2 border-gray-500 bg-white p-3"
          />
        </div>
      )}
    </main>
  );
}
