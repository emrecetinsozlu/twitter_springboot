import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"

import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import { loginSchema } from "@/schemas/authSchema"
import { useLogin } from "@/hooks/useAuth"



export default function LoginPage() {

  const loginMutation = useLogin()



  const form = useForm({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      username: "",
      password: "",
    },
  })



  function onSubmit(values) {
    loginMutation.mutate(values)
    
  }

  return (
    <div className="flex min-h-screen items-center justify-center px-4">
      <div className="w-full max-w-sm space-y-6">
        <div className="space-y-2 text-center">
          <h1 className="text-2xl font-bold tracking-tight">Giriş yap</h1>
          <p className="text-sm text-muted-foreground">
            Hesabına giriş yaparak devam et
          </p>
        </div>

        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
          <div className="space-y-2">
            <label htmlFor="username" className="text-sm font-medium">
              Kullanıcı Adı
            </label>
            <input
              id="username"
              type="text"
              placeholder="Emre"
              {...form.register("username")}
              className="flex h-9 w-full rounded-md border border-input bg-background px-3 text-sm outline-none focus-visible:border-ring focus-visible:ring-3 focus-visible:ring-ring/50"
            />
            {form.formState.errors.username && <p className="text-red-500">{form.formState.errors.username.message}</p>}
          </div>

          <div className="space-y-2">
            <label htmlFor="password" className="text-sm font-medium">
              Şifre
            </label>
            <input
              id="password"
              type="password"
              placeholder="••••••••"
              {...form.register("password")}
              className="flex h-9 w-full rounded-md border border-input bg-background px-3 text-sm outline-none focus-visible:border-ring focus-visible:ring-3 focus-visible:ring-ring/50"
            />
            {form.formState.errors.password && <p className="text-red-500">{form.formState.errors.password.message}</p>}
          </div>

          <Button type="submit" className="w-full" disabled={loginMutation.isPending}>
             {loginMutation.isPending ? "Giriş yapılıyor..." : "Giriş Yap"}
          </Button>
        </form>

        <p className="text-center text-sm text-muted-foreground">
          Hesabın yok mu?{" "}
          <Link to="/register" className="font-medium text-primary hover:underline">
            Kayıt ol
          </Link>
        </p>
      </div>
    </div>
  )
}
