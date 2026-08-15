import { Link, NavLink } from 'react-router-dom'
import {
  Bookmark,
  Home,
  User,
} from 'lucide-react'
//import { useMe } from '@/hooks/useAuth'
import { useAuthStore } from '@/store/authStore'

const sidebarLinks = [
  { label: 'Home', Icon: Home, to: '/', active: true, isProtected: false },
  { label: 'Bookmarks', Icon: Bookmark, to: '/bookmarks', isProtected: true },
  { label: 'Profile', Icon: User, to: '/profile', isProtected: true },
]



export function Sidebar() {
 
    //const { data: user } = useMe();
  //kullanıcı bilgisini artık zustand store'dan alacağız
    const user = useAuthStore((state) => state.user)
    
  

  // 2. Eğer kullanıcı login değilse (veya yetkisizse) korumalı linkleri eliyoruz
    const visibleLinks = sidebarLinks.filter(link => {
    if (link.isProtected && !user) {
       return false // Kullanıcı giriş yapmadıysa korumalı linki gösterme
    }
       return true // Diğer durumlarda göster
    })

  return (
    <nav className="sticky top-0 hidden h-screen w-48 flex-shrink-0 flex-col bg-background px-3 py-6 md:flex lg:w-64">
      <div className='mb-8 px-4'>
        <Link
          to='/'
          className='font-heading text-2xl font-bold italic tracking-tighter text-neon-pink text-glow-primary'
        >
          Neon Link
        </Link>
      </div>

      <div className='flex-1 space-y-2'>
        {visibleLinks.map(({ label, Icon, to }) => {
          return (
            <NavLink
              key={label}
              to={to}
              // NavLink'in className'i aktif sayfayı algılayıp neon stillerini otomatik basar
              className={({ isActive }) =>
                `group flex items-center gap-4 rounded-full px-4 py-3 transition-all duration-300 hover:bg-surface-container-highest active:scale-95 ${
                  isActive
                    ? 'font-bold text-neon-pink drop-shadow-[0_0_8px_rgba(255,45,120,0.8)]'
                    : 'text-muted-foreground'
                }`
              }
            >
              {/* İkonun aktif sayfada içinin dolması (fill) için yine isActive durumunu dinliyoruz */}
              {({ isActive }) => (
                <>
                  <Icon
                    className={`size-6 transition-colors group-hover:text-neon-cyan ${
                      isActive ? 'fill-current' : ''
                    }`}
                    aria-hidden='true'
                  />
                  <span className='font-heading text-lg transition-colors group-hover:text-neon-cyan'>
                    {label}
                  </span>
                </>
              )}
            </NavLink>
          )
        })}
      </div>
    </nav>
  )
}
