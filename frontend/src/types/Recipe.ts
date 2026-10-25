import type { RecipeVisibility } from "./RecipeVisibility";

//Recipeオブジェクトの型定義
export type Recipe = {
  id: number;
  name: string;
  description: string;
  cookedDate: string | null;
  visibility: RecipeVisibility;
  imageUrl: string | null;
};
