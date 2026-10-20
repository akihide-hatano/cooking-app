const TOKEN_KEY = "token";

export const saveToken = (token: string): void => {
  sessionStorage.setItem(TOKEN_KEY, token);
};

export const getToken = (): string | null => {
  return sessionStorage.getItem(TOKEN_KEY);
};

export const removeToken = (): void => {
  sessionStorage.removeItem(TOKEN_KEY);
};
