
import { Outlet } from "react-router-dom"
import { Sidebar } from "@/components/Sidebar"

const Layout = () => {
  return (
    // flex-row sayesinde Sidebar solda, içerik sağda yan yana duracak
    <div className="min-h-screen md:grid md:grid-cols-[1fr_minmax(0,36rem)_1fr]">
      
      {/* Her sayfada sabit kalacak olan Sidebar */}
      <div className="hidden md:flex md:justify-end">
        <Sidebar />
      </div>

      {/* Rotalara göre değişecek olan dinamik sayfa içerikleri (Home, Profile, Bookmark) buraya dolacak */}
      <main className="mx-auto min-h-screen w-full max-w-xl border-x border-border md:mx-0">
        <Outlet />
      </main>

    </div>
  )
}

export default Layout