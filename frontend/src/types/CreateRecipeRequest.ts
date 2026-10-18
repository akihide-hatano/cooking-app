export type CreateRecipeRequest = {
  name: string;
  description: string;
  cookedDate: string;
  visibility: "PRIVATE" | "FAMILY" | "PUBLIC";
};
