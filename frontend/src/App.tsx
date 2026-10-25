import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom";
import { LoginPage } from "./features/auth/LoginPage";
import { RegisterPage } from "./features/auth/RegisterPage";
import { useParams } from "react-router-dom";
import RecipeListPage from "./features/recipes/RecipeListPage";
import RecipeCreatePage from "./features/recipes/RecipeCreatePage";
import RecipeDetailPage from "./features/recipes/RecipeDetailPage";

function RecipeDetailRoute() {
  const { id } = useParams<{ id: string }>();

  return <RecipeDetailPage key={id} />;
}
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/recipes" element={<RecipeListPage />} />
        <Route path="/recipes/new" element={<RecipeCreatePage />} />
        <Route path="/recipes/:id" element={<RecipeDetailRoute />} />
        <Route path="/recipes/:id/edit" element={<RecipeCreatePage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
