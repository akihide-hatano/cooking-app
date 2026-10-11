//Recipeオブジェクトの型定義
export type Recipe = {
  id: number;
  name: string;
  description: string;
  cookedDate: string | null;
  visibility: "PRIVATE" | "FAMILY" | "PUBLIC";
  imageUrl: string | null;
};
