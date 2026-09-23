import { useState } from "react";
import { register } from "../../api/auth";

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

      <div className="flex flex-col gap-4">
        <div>
          <label>Name</label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>
        <div>
          <label>Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>
        <div>
          <label>Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        {errorMessage && <p className="text-red-500">{errorMessage}</p>}
        {successMessage && <p className="text-green-500">{successMessage}</p>}
        <button
          className="bg-blue-500 text-white px-4 rounded py-2 hover:bg-blue-600 transition-colors"
          onClick={handleRegister}
        >
          Register
        </button>
      </div>
    </div>
  );
};
