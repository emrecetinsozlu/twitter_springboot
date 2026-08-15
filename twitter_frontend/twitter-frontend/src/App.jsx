import { createBrowserRouter, RouterProvider } from "react-router-dom"
import HomePage from "@/pages/HomePage"
import LoginPage from "@/pages/LoginPage"
import RegisterPage from "@/pages/RegisterPage"
import { ProtectedRoute } from "@/components/protectedRoute/ProtectedRoute"
import ProfilePage from "@/pages/ProfilePage"
import BookmarkPage from "@/pages/BookmarkPage"
import TweetDetailPage from "@/pages/TweetDetailPage"
import Layout from "@/components/Layout"
import { AuthInitializer } from "@/components/AuthInitializer"



const router = createBrowserRouter([
  {
    // HERKESE AÇIK LAYOUT (Giriş yapan da yapmayan da burayı görür)
    element: <Layout />, 
    children: [
      {
        path: "/",
        element: <HomePage />, // Herkes tweetleri okuyabilir
      },
      {
        path: "/home",
        element: <HomePage />, // Herkes tweetleri okuyabilir
      },
      {
        path: "/tweets/:tweetId",
        element: <TweetDetailPage />,
      },
      
      // SADECE GİRİŞ YAPANLARIN ERİŞEBİLECEĞİ KORUMALI ALAN
      {
        element: <ProtectedRoute />, 
        children: [
          {
            path: "/profile",
            element: <ProfilePage />, 
          },
          {
            path: "/bookmarks",
            element: <BookmarkPage />, 
          },
        ],
      },
    ],
  },
  {
    path: "/login",
    element: <LoginPage />,
  },
  {
    path: "/register",
    element: <RegisterPage />,
  },
])

export default function App() {
  return (
    <>
      <AuthInitializer />
      <RouterProvider router={router} />
    </>
  )
}
