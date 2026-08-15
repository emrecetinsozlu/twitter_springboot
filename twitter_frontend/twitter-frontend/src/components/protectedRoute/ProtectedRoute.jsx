// src/routes/ProtectedRoute.jsx
import { Navigate, Outlet } from "react-router-dom"
import { useMe } from "@/hooks/useAuth"

export function ProtectedRoute() {
  const { data: user, isLoading, isError } = useMe()

  if (isLoading) {
    return <div>Yükleniyor...</div>
  }

  if (isError || !user) {
    return <Navigate to="/login" replace />
  }

  return <Outlet />
}