import { useParams } from "react-router-dom";

export default function RecipeDetailPage() {
  const { id } = useParams<{ id: string }>();

  return (
    <div>
      <h1>Recipe Detail Page</h1>
      <p>Recipe ID: {id}</p>
    </div>
  );
}
