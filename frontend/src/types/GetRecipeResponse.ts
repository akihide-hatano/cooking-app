import type { RecipeVisibility } from "./types/RecipeVisibility";

export type GetRecipeResponse = {
  id: number;
  name: string;
  description: string;
  cookedDate: string | null;
  visibility: RecipeVisibility;
};
