import { useState } from "react";

// import { useNavigate } from "react-router-dom";

export default function RecipeCreatePage() {
  //   const navigate = useNavigate();
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [cookDate, setCookDate] = useState("");
  //   const [visibility, setVisibility] = useState<Visibility>("FAMILY");
  //   const [imageFile, setImageFile] = useState<File | null>(null);

  return (
    <main>
      <h1>レシピを登録する</h1>

      <label htmlFor="name">料理名</label>
      <input
        id="name"
        type="text"
        value={name}
        onChange={(e) => setName(e.target.value)}
        placeholder="料理名を入力してください"
        className="w-full rounded-lg border-2 border-gray-500 bg-white p-3"
      />

      <label htmlFor="description">料理の説明</label>
      <textarea
        id="description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        placeholder="料理の説明を入力してください"
        className="w-full rounded-lg border-2 border-gray-500 bg-white p-3"
      ></textarea>

      <label htmlFor="cookDate">調理日</label>
      <input
        id="cookDate"
        type="date"
        value={cookDate}
        onChange={(e) => setCookDate(e.target.value)}
        className="w-full rounded-lg border-2 border-gray-500 bg-white p-3"
      />
    </main>
  );
}
