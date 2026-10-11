import type { Recipe } from "../../../types/Recipe";

type RecipeCardProps = {
  recipe: Recipe;
};

export default function RecipeCard({ recipe }: RecipeCardProps) {
  return (
    <article className="flex h-full flex-col overflow-hidden rounded-card border border-border-soft bg-surface shadow-sm">
      {/* 料理写真 */}
      <div className="aspect-[4/3] w-full bg-brand-light">
        {recipe.imageUrl ? (
          <img
            src={recipe.imageUrl}
            alt={recipe.name}
            className="h-full w-full object-cover"
          />
        ) : (
          <div className="flex h-full items-center justify-center text-brand">
            写真なし
          </div>
        )}
      </div>

      {/* 料理情報 */}
      <div className="flex flex-1 flex-col p-4">
        <h3 className="text-base font-bold text-text-main md:text-lg">
          {recipe.name}
        </h3>

        <p className="mt-2 text-sm line-clamp-2 leading-relaxed text-text-muted">
          {recipe.description}
        </p>

        <span className="mt-auto pt-4 text-sm font-bold text-brand">
          詳しく見る →
        </span>
      </div>
    </article>
  );
}
