import { useNavigate } from "react-router-dom";

export default function BackButton() {
  const navigate = useNavigate();

  return (
    <button
      type="button"
      onClick={() => navigate(-1)}
      className="text-sm font-semibold text-green-800 hover:underline"
    >
      ← 戻る
    </button>
  );
}
