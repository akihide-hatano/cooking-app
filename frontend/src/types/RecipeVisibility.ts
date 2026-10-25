export type RecipeVisibility = "PRIVATE" | "FAMILY" | "PUBLIC";

export const RECIPE_VISIBILITY_LABELS: Record<RecipeVisibility, string> = {
  PRIVATE: "非公開",
  FAMILY: "家族のみ",
  PUBLIC: "公開",
};

export const RECIPE_VISIBILITY_OPTIONS: {
  value: RecipeVisibility;
  label: string;
}[] = [
  { value: "FAMILY", label: "家族のみ" },
  { value: "PRIVATE", label: "自分のみ" },
  { value: "PUBLIC", label: "公開" },
];
