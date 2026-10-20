import type { CreateRecipeRequest } from "../types/CreateRecipeRequest";
import { getToken, removeToken } from "../utils/tokenStorage";

const API_URL = import.meta.env.VITE_API_URL;

export const createRecipe = async (recipeData: CreateRecipeRequest) => {
  const token = getToken();

  if (!token) {
    throw new Error("ログインが必要です");
  }
  const response = await fetch(`${API_URL}/recipes/create`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(recipeData),
  });

  if (response.status === 401) {
    removeToken();
    throw new Error("認証に失敗しました。ログインが必要です");
  }

  if (!response.ok) {
    throw new Error("レシピの作成に失敗しました");
  }
  return response.json();
};
