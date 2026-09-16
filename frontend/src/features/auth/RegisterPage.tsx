import { useState } from "react";
import { register } from "../../api/auth";
import { Link } from "react-router-dom";

export const RegisterPage = () => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const handleRegister = async () => {
    try {
      setErrorMessage("");
      setSuccessMessage("");
      await register({ name, email, password });
      setSuccessMessage("登録が成功しました");
    } catch (error) {
      console.error("Registration failed:", error);
      if (error instanceof Error) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage("登録に失敗しました");
      }
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center gap-6">
      <h1 className="text-3xl font-bold">Register Page</h1>

      <div className="flex flex-col gap-4 w-50">
        <div className="flex flex-col">
          <label>Name</label>
          <input
            className="border border-gray-300 rounded px-2 py-1"
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>
        <div className="flex flex-col">
          <label>Email</label>
          <input
            className="border border-gray-300 rounded px-2 py-1"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>
        <div className="flex flex-col">
          <label>Password</label>
          <input
            className="border border-gray-300 rounded px-2 py-1"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        {errorMessage && <p className="text-red-500 text-sm">{errorMessage}</p>}
        {successMessage && (
          <p className="text-green-500 text-sm">{successMessage}</p>
        )}
        <button
          className="bg-blue-500 text-white px-4 rounded py-2 hover:bg-blue-600 transition-colors"
          onClick={handleRegister}
        >
          Register
        </button>

        <p>
          すでにアカウントをお持ちですか？
          <Link to="/login" className="ml-2 text-blue-500 hover:underline">
            Login
          </Link>
        </p>
      </div>
    </div>
  );
};
