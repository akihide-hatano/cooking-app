import type { CreateRecipeRequest } from "../types/CreateRecipeRequest";
import type { RecipeDetail } from "../types/RecipeDetail";
import type { UpdateRecipeRequest } from "../types/UpdateRecipeRequest";
import type { UpdateRecipeResponse } from "../types/UpdateRecipeResponse";
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

export const getRecipeDetail = async (id: number): Promise<RecipeDetail> => {
  const token = getToken();

  if (!token) {
    throw new Error("ログインが必要です");
  }

  const response = await fetch(`${API_URL}/recipes/${id}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (response.status === 401) {
    removeToken();
    throw new Error("認証に失敗しました。ログインが必要です");
  }

  if (response.status === 403) {
    throw new Error("このレシピを閲覧する権限がありません");
  }

  if (response.status === 404) {
    throw new Error("レシピが見つかりません");
  }

  if (!response.ok) {
    throw new Error("レシピの取得に失敗しました");
  }

  return (await response.json()) as RecipeDetail;
};

export const updateRecipe = async (
  id: number,
  recipeData: UpdateRecipeRequest,
): Promise<UpdateRecipeResponse> => {
  const token = getToken();

  if (!token) {
    throw new Error("ログインが必要です");
  }
  const response = await fetch(`${API_URL}/recipes/${id}`, {
    method: "PUT",
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

  if (response.status === 403) {
    throw new Error("このレシピを更新する権限がありません");
  }

  if (response.status === 404) {
    throw new Error("レシピが見つかりません");
  }

  if (!response.ok) {
    throw new Error("レシピの更新に失敗しました");
  }

  return (await response.json()) as UpdateRecipeResponse;
};

export const deleteRecipe = async (id: number): Promise<void> => {
  const token = getToken();
  if (!token) {
    throw new Error("ログインが必要です");
  }

  const response = await fetch(`${API_URL}/recipes/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (response.status === 401) {
    removeToken();
    throw new Error("認証に失敗しました。ログインが必要です");
  }

  if (response.status === 403) {
    throw new Error("このレシピを削除する権限がありません");
  }

  if (response.status === 404) {
    throw new Error("レシピが見つかりません");
  }

  if (!response.ok) {
    throw new Error("レシピの削除に失敗しました");
  }
};
