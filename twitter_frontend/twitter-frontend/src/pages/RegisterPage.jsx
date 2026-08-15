import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"

import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import { registerSchema } from "@/schemas/authSchema"
import { useRegister } from "@/hooks/useAuth"

export default function RegisterPage() {

  const registerMutation = useRegister()

  const form = useForm({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      username: "",
      email: "",
      password: "",
      confirmPassword: "",
    },
  })


  const onSubmit = (values) => {
    console.log(values)
    registerMutation.mutate(values)
  }

  return (
    <div className="flex min-h-screen items-center justify-center px-4">
      <div className="w-full max-w-sm space-y-6">
        <div className="space-y-2 text-center">
          <h1 className="text-2xl font-bold tracking-tight">Hesap oluştur</h1>
          <p className="text-sm text-muted-foreground">
            Yeni bir hesap oluşturarak başla
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
              placeholder="Kullanıcı Adı"
              {...form.register("username")}
              className="flex h-9 w-full rounded-md border border-input bg-background px-3 text-sm outline-none focus-visible:border-ring focus-visible:ring-3 focus-visible:ring-ring/50"
            />
            {form.formState.errors.name && <p className="text-red-500">{form.formState.errors.name.message}</p>}
          </div>

       
          <div className="space-y-2">
            <label htmlFor="email" className="text-sm font-medium">
              Email
            </label>
            <input
             
              id="email"
              type="email"
              placeholder="schnupps10@hotmail.com"
              {...form.register("email")}
              className="flex h-9 w-full rounded-md border border-input bg-background px-3 text-sm outline-none focus-visible:border-ring focus-visible:ring-3 focus-visible:ring-ring/50"
            />
            {form.formState.errors.email && <p className="text-red-500">{form.formState.errors.email.message}</p>}
          </div>

          <div className="space-y-2">
            <label htmlFor="password" className="text-sm font-medium">
              Şifre
            </label>
            <input
              id="password"
              type="password"
              placeholder="••••••••"
              className="flex h-9 w-full rounded-md border border-input bg-background px-3 text-sm outline-none focus-visible:border-ring focus-visible:ring-3 focus-visible:ring-ring/50"
              {...form.register("password")}
            />
            {form.formState.errors.password && <p className="text-red-500">{form.formState.errors.password.message}</p>}
          </div>
          <div className="space-y-2">
            <label htmlFor="confirmPassword" className="text-sm font-medium">
              Şifre Tekrarı
            </label>
            <input
              id="confirmPassword"
              type="password"
              placeholder="••••••••"
              className="flex h-9 w-full rounded-md border border-input bg-background px-3 text-sm outline-none focus-visible:border-ring focus-visible:ring-3 focus-visible:ring-ring/50"
              {...form.register("confirmPassword")}
            />
            {form.formState.errors.confirmPassword && <p className="text-red-500">{form.formState.errors.confirmPassword.message}</p>}
          </div>

          <Button type="submit" className="w-full" disabled={registerMutation.isPending}>
            Kayıt ol
          </Button>
        </form>

        <p className="text-center text-sm text-muted-foreground">
          Zaten hesabın var mı?{" "}
          <Link to="/login" className="font-medium text-primary hover:underline">
            Giriş yap
          </Link>
        </p>
      </div>
    </div>
  )
}
