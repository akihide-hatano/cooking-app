import { useState } from "react";
import { login } from "../../api/auth";

export const LoginPage = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async () => {
    // Implement login logic here
    try {
      const response = await login({ email, password });
      console.log("Login successful:", response);
    } catch (error) {
      console.error("Login failed:", error);
    }
  };

  return (
    <div className="min-h-screen flex justify-center items-center flex-col gap-6">
      <h1 className="text-3xl font-bold">Login Page</h1>

      <div className="flex flex-col gap-4">
        <div className="flex flex-col">
          <label>Email:</label>
          <input
            className="border rounded px-3 py-2"
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>

        <div className="flex flex-col">
          <label>Password:</label>
          <input
            className="border rounded px-3 py-2"
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
      </div>

      <button
        className="border rounded px-6 py-2 bg-blue-500 text-white hover:bg-blue-600 transition-colors"
        type="button"
        onClick={handleLogin}
      >
        Login
      </button>
    </div>
  );
};
