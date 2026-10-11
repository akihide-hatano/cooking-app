import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom";
import { LoginPage } from "./features/auth/LoginPage";
import { RegisterPage } from "./features/auth/RegisterPage";
import RecipeListPage from "./features/recipes/RecipeListPage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        <Route path="/recipes" element={<RecipeListPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
