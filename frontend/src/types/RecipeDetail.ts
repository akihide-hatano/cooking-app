export type RecipeStep = {
  id: number;
  description: string;
  sortOrder: number;
};

export type RecipeDetail = {
  id: number;
  name: string;
  description: string;
  cookedDate: string | null;
  visibility: "PRIVATE" | "FAMILY" | "PUBLIC";
  imageUrl: string | null;
  steps: RecipeStep[];
};
