import type { RecipeVisibility } from "./RecipeVisibility";

export type UpdateRecipeResponse = {
  id: number;
  name: string;
  description: string | null;
  cookedDate: string;
  visibility: RecipeVisibility;
};
