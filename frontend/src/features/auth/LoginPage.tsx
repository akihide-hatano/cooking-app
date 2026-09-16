import { useState } from "react";
import { login } from "../../api/auth";
import { Link, useNavigate } from "react-router-dom";

export const LoginPage = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleLogin = async () => {
    // Implement login logic here
    try {
      //欲しいdataをloginから取得する
      const response = await login({ email, password });
      console.log("Login successful:", response);

      //strageへtokenを保存する
      localStorage.setItem("token", response.token);
      navigate("/user");

      //setsuccessメッセージを表示する
      setSuccess("ログインに成功しました");
      setError("");
    } catch (error) {
      console.error("Login failed:", error);
      setError((error as Error).message || "ログインに失敗しました");
      //setsuccessメッセージを非表示にする
      setSuccess("");
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
      {error && <p className="text-red-500">{error}</p>}
      {success && <p className="text-green-500">{success}</p>}

      <p className="mt-4 text-sm text-gray-600">
        アカウントをお持ちでないですか？{" "}
        <Link to="/register" className="text-blue-500 hover:underline">
          新規登録はこちら
        </Link>
      </p>
    </div>
  );
};
