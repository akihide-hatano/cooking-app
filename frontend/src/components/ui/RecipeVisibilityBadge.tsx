import type { RecipeVisibility } from "../../types/RecipeVisibility";
import { RECIPE_VISIBILITY_LABELS } from "../../constants/recipeVisibility";

type props = {
  visibility: RecipeVisibility;
};

const visibilityStyles: Record<RecipeVisibility, string> = {
  FAMILY: "bg-green-100 text-green-800",
  PRIVATE: "bg-gray-100 text-gray-700",
  PUBLIC: "bg-blue-100 text-blue-800",
};

export default function RecipeVisibilityBadge({ visibility }: props) {
  return (
    <span
      className={`inline-flex items-center gap-3 rounded px-2 py-1 text-xs font-bold ${visibilityStyles[visibility]}`}
    >
      {RECIPE_VISIBILITY_LABELS[visibility]}
    </span>
  );
}
