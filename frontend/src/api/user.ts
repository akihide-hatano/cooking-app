const API_URL = import.meta.env.VITE_API_URL;

export const getMe = async () => {
  const token = localStorage.getItem("token");
  const response = await fetch(`${API_URL}/users/me`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.error || "ユーザー情報の取得に失敗しました");
  }
  return response.json();
};
