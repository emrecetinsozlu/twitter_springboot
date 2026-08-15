import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { getMeRequest, loginRequest, registerRequest, deleteUserRequest, logoutRequest } from "@/services/authService"
import { useNavigate } from "react-router-dom"
import { toast } from "react-toastify"
import { useAuthStore } from "@/store/authStore"
import { useEffect } from "react"

/*
export function useMe(options = {}) {
  return useQuery({
    queryKey: ["me"],
    queryFn: getMeRequest,
    retry: false,
    staleTime: 1000 * 60 * 5,
    refetchOnWindowFocus: false,
    refetchOnReconnect: false,
    refetchOnMount: false,
    ...options // Dışarıdan gelen 'enabled' gibi ayarların useQuery'ye aktarılmasını sağlar
  })
}
*/

export function useMe(){
  const setUser = useAuthStore((state) => state.setUser)
  const clearUser = useAuthStore((state) => state.clearUser)

  const query = useQuery({
    queryKey: ["me"],
    queryFn: getMeRequest,
    retry: false,
    staleTime: 1000 * 60 * 5,
    refetchOnWindowFocus: false,
    refetchOnReconnect: false,
    refetchOnMount: false,
  })

  useEffect(() => {
    if (query.data) {
      setUser(query.data)
    } else {
      clearUser()
    }
  }, [query.data, setUser, clearUser])

  return query;
}


/*
// 2. YENİ: Sadece hafızadaki (cache) kullanıcı verisine bakan hook
export function useIsAuthenticated() {
  const queryClient = useQueryClient();
  
  // Hafızadaki "me" sorgusunun datasını oku
  const cachedUser = queryClient.getQueryData(["me"]);
  
  // Eğer hafızada kullanıcı varsa true, yoksa false döner
  return !!cachedUser;
}
*/


/*
loginRequest()
↓
cookie backend tarafından yazılır
↓
getMe() çağrılır (backend tarafından gelen kullanıcı bilgisini alır)
↓
React Query ["me"] full user ile dolar
↓
Zustand store full user ile dolar
↓
navigate("/")
*/

export function useLogin() {
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const setUser = useAuthStore((state) => state.setUser)
  return useMutation({
    mutationFn: loginRequest,
    onSuccess: async () => {
      
      // direkt "me" sorgusunun hafızasına (cache) enjekte ediyoruz.
      //fetchQuery ile setQuery arasındaki fark; biri manuel diğer ise query ile doldurmak için.
      const fullUserData = await queryClient.fetchQuery({
        queryKey: ["me"],
        queryFn: getMeRequest,
      })
   
      setUser(fullUserData)
      navigate("/")
      toast.success("Giriş başarılı")
    },
    onError: (error) => {
      toast.error(error.response.data.message)
     
    },
  })
}


export function useLogout() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()
  const clearUser = useAuthStore((state) => state.clearUser)
  return useMutation({
    mutationFn: logoutRequest,
    onSuccess: () => {
      queryClient.removeQueries({ queryKey: ["me"] })
      clearUser()
      navigate("/login")
      toast.success("Çıkış başarılı")
    },
    onError: (error) => {
      toast.error(error.response.data.message)
    },
  })
}


export function useRegister() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()
  return useMutation({
    mutationFn: registerRequest,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["me"] })
      navigate("/login")
      toast.success("Kayıt başarılı")
     
    },
    onError: (error) => {
      toast.error(error.response.data.message)
    },
  })
}

export function useDeleteUser() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()
  const clearUser = useAuthStore((state) => state.clearUser)
  return useMutation({
    mutationFn: deleteUserRequest,
    onSuccess: () => {
      queryClient.removeQueries({ queryKey: ["me"] })
      clearUser()
      navigate("/login")
      toast.success("Hesap silindi")
    },
    onError: (error) => {
      toast.error(error.response.data.message)
    },
  })
}