import type { Recipe } from "../../types/Recipe";
import RecipeCard from "./components/RecipeCard";
import PrimaryButton from "../../components/ui/PrimaryButton";

const recipes: Recipe[] = [
  {
    id: 1,
    name: "肉じゃが",
    description: "母の得意料理",
    cookedDate: "2026-10-11",
    visibility: "FAMILY",
    imageUrl:
      "https://commons.wikimedia.org/wiki/Special:FilePath/Nikujaga.jpg?width=640",
  },
  {
    id: 2,
    name: "カレーライス",
    description: "家族みんなが大好きなカレー",
    cookedDate: "2026-10-10",
    visibility: "PUBLIC",
    imageUrl:
      "https://commons.wikimedia.org/wiki/Special:FilePath/Kare-Raisu.jpg?width=640",
  },
  {
    id: 3,
    name: "だし巻き卵",
    description: "父の得意料理",
    cookedDate: null,
    visibility: "PRIVATE",
    imageUrl:
      "https://commons.wikimedia.org/wiki/Special:FilePath/Dashimaki_tamago_by_jetalone_in_Kyobashi%2C_Tokyo.jpg?width=640",
  },
];

export default function RecipeListPage() {
  return (
    <main className="min-h-screen bg-cream px-4 py-6 md:px-8">
      <div className="mx-auto max-w-6xl">
        <div className="flex justify-between items-center mb-4">
          <h1 className="text-2xl font-bold text-text-main md:text-3xl">
            我が家のレシピ
          </h1>
          <div className="flex items-baseline">
            <span className="text-2xl">{recipes.length}</span>
            <span className="text-xs">品</span>
          </div>
        </div>
        <div className="mb-6 flex justify-end">
          <PrimaryButton fullWidth={false}>＋ 料理を追加する</PrimaryButton>
        </div>
        {recipes.length === 0 ? (
          <div className="rounded-card bg-white px-6 py-16 text-center">
            <p className="mb-2 text-sm font-bold text-text-main">
              レシピがありません
            </p>
            <p className="text-sm text-text-muted">
              まずはレシピを追加してみましょう
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-2 gap-4 lg:grid-cols-3">
            {recipes.map((recipe) => (
              <RecipeCard key={recipe.id} recipe={recipe} />
            ))}
          </div>
        )}
      </div>
    </main>
  );
}
