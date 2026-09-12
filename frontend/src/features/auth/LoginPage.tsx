export const LoginPage = () => {
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
          />
        </div>

        <div className="flex flex-col">
          <label>Password:</label>
          <input
            className="border rounded px-3 py-2"
            type="password"
            placeholder="Password"
          />
        </div>
      </div>

      <button
        className="border rounded px-6 py-2 bg-blue-500 text-white hover:bg-blue-600 transition-colors"
        type="submit"
      >
        Login
      </button>
    </div>
  );
};
