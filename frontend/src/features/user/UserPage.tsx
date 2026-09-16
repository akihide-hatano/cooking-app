import { useState, useEffect } from "react";
import { getMe } from "../../api/user";

import { Swiper, SwiperSlide } from "swiper/react";
import { Autoplay } from "swiper/modules";

import { Link } from "react-router-dom";

import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";

export const UserPage = () => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const fetchMe = async () => {
      try {
        const user = await getMe();
        console.log("user", user);
        setUser(user);
      } catch (error) {
        console.error(error);
      }
    };

    fetchMe();
  }, []);

  const recipes = [
    { id: 1, name: "肉じゃが" },
    { id: 2, name: "カレー" },
    { id: 3, name: "唐揚げ" },
    { id: 4, name: "親子丼" },
    { id: 5, name: "ハンバーグ" },
  ];

  return (
    <div className="min-h-screen p-8">
      <h1 className="text-3xl font-bold">User Page</h1>

      {user && (
        <div className="mt-4">
          <p className="text-xl">名前: {user.name}</p>
          <p className="mt-2 text-gray-600">今日は何をしましょうか？</p>
        </div>
      )}

      <div className="flex gap-4 mt-8">
        <Link
          to="/recipes/new"
          className="bg-blue-500 text-white px-6 py-3 rounded-lg hover:bg-blue-600"
        >
          レシピを投稿
        </Link>

        <Link
          to="/recipes"
          className="border px-6 py-3 rounded-lg hover:bg-gray-100"
        >
          自分のレシピ
        </Link>

        <Link
          to="/favorites"
          className="border px-6 py-3 rounded-lg hover:bg-gray-100"
        >
          お気に入り
        </Link>
      </div>

      <h2 className="text-2xl font-bold mt-10 mb-4">家族のレシピ</h2>

      <Swiper
        modules={[Autoplay]}
        autoplay={{ delay: 3000 }}
        onAutoplay={{
          delay: 0,
          disableOnInteraction: false,
        }}
        spaceBetween={16}
        slidesPerView={3}
      >
        {recipes.map((recipe) => (
          <SwiperSlide key={recipe.id}>
            <div className="border rounded-lg p-6 shadow">
              <p className="text-xl font-bold">{recipe.name}</p>
            </div>
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
};
