import type { RecipeVisibility } from "./RecipeVisibility";

export type GetRecipeResponse = {
  id: number;
  name: string;
  description: string;
  cookedDate: string | null;
  visibility: RecipeVisibility;
};
