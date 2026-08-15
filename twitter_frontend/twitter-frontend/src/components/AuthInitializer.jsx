import { useMe } from "@/hooks/useAuth";
import { useAuthStore } from "@/store/authStore"
import { useEffect } from "react"
export function AuthInitializer() {
  const setUser = useAuthStore((state) => state.setUser)
  const clearUser = useAuthStore((state) => state.clearUser)

  const { data: user, isError, isLoading } = useMe()

  useEffect(() => {
    if (user) {
      setUser(user)
    }

    if (isError) {
      clearUser()
    }
  }, [user, isError, setUser, clearUser])

  if (isLoading) return null

  return null
}