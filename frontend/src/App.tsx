import { BrowserRouter, Route, Routes } from "react-router-dom";
import { LoginPage } from "./features/auth/LoginPage";
import { RegisterPage } from "./features/auth/RegisterPage";
import { UserPage } from "./features/user/UserPage";
import { RecipeCreatePage } from "./features/recipe/RecipeCreatePage";
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/user" element={<UserPage />} />
        <Route path="/recipes/new" element={<RecipeCreatePage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
