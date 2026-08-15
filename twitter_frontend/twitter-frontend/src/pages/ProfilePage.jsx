import { useMe } from '@/hooks/useAuth'
import { Button } from '@/components/ui/button'
import { LogOut, Delete } from 'lucide-react'
import { useLogout, useDeleteUser } from '@/hooks/useAuth'

const ProfilePage = () => {
  const { data: user } = useMe()
  const logoutMutation = useLogout()
  const deleteUserMutation = useDeleteUser()

  const handleLogout = () => {
    logoutMutation.mutate()
  }
  const handleDeleteUser = () => {
    deleteUserMutation.mutate()
  }

  const createdAt = new Date(user?.createdAt).toLocaleDateString()

  return (
    <div className='container mx-auto'>
      <div className='flex flex-col items-center justify-start h-screen py-10 px-4 gap-4'>
        <h2 className='text-2xl font-bold text-center py-4'>Profile Page</h2>
        <p className='text-sm text-muted-foreground text-center'>
          Merhaba {user?.username}!
        </p>

        <p className='text-sm text-muted-foreground'>
          Hesabınız oluşturulma tarihi: {createdAt}
        </p>
        <div className='flex gap-2'>
          <Button variant='outline' onClick={handleLogout}>
            <LogOut className='h-4 w-4' />
            Çıkış Yap
          </Button>
          <Button variant='destructive' onClick={handleDeleteUser}>
            <Delete className='h-4 w-4' />
            Hesabı Sil
          </Button>
        </div>
      </div>
    </div>
  )
}

export default ProfilePage
