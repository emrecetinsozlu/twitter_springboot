import { Link } from 'react-router-dom'
import { Button } from '@/components/ui/button'

import { useEffect, useRef } from 'react'
import { useInfiniteTweets } from '@/hooks/useInfiniteTweets'
import { TweetList } from '@/components/TweetList'
import TweetCardSkeleton from '@/components/tweetCard/TweetCardSkeleton'

import { TweetComposer } from '@/components/TweetComposer'
import { useAuthStore } from '@/store/authStore'


export default function HomePage() {


 // Hafızada kullanıcı var mı kontrol et
 //const { data: user } = useMe();
 //kullanıcı bilgisini artık zustand store'dan alacağız
 const user = useAuthStore((state) => state.user)
 const isAuthenticated = !!user;



  const {
    data,
    isLoading,
    isError,
    error,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  } = useInfiniteTweets()

 
  const loadMoreRef = useRef(null)
  const tweets = data?.pages.flatMap((page) => page.content) ?? []
  /*
  console.log('pages:', data?.pages)
  console.log('tweets length:', tweets.length)
  console.log('hasNextPage:', hasNextPage)
  console.log('isFetchingNextPage:', isFetchingNextPage)
  */
  useEffect(() => {
    // sayfa sonuna gelindiğinde daha fazla tweet yüklemek için kullanacağız
    const target = loadMoreRef.current
    if (!target) return
    
    const observer = new IntersectionObserver(
      (entries) => {
        const firstEntry = entries[0]

        console.log('observer çalıştı:', firstEntry.isIntersecting)

        if (firstEntry.isIntersecting && hasNextPage && !isFetchingNextPage) {
          console.log('fetchNextPage çağrıldı')
          fetchNextPage()
        }
      },
      {
        rootMargin: '200px',
        threshold: 0.1,
      },
    )

    observer.observe(target)

    return () => {
      observer.disconnect()
    }
  }, [fetchNextPage, hasNextPage, isFetchingNextPage])

  if (isLoading) {
    return (
      <section>
        {Array.from({ length: 10 }).map((_, index) => (
          <TweetCardSkeleton key={index} />
        ))}
      </section>
    );
  }

  if (isError) {
    return (
      <div className='mx-auto max-w-xl p-4'>
        <p>Tweetler yüklenirken hata oluştu: {error.message}</p>
      </div>
    )
  }

  return (
    <div>
      <header className='sticky top-0 z-10 border-b border-border bg-background/80 px-4 py-3 backdrop-blur-sm'>
        <div className='flex items-center justify-between'>
          <h1 className='text-lg font-bold'>Ana Sayfa</h1>
          {user ? (
            <div className='flex items-center gap-2'>
              <span className='text-sm text-muted-foreground'>
                {user.username}
              </span>
            </div>
          ) : (
            <div className='flex gap-2'>
              <Button variant='outline' size='sm' asChild>
                <Link to='/login'>Giriş yap</Link>
              </Button>
              <Button size='sm' asChild>
                <Link to='/register'>Kayıt ol</Link>
              </Button>
            </div>
          )}
        </div>
      </header>

      <TweetComposer isAuthenticated={isAuthenticated} />
      <section className='divide-y divide-border'>
        <TweetList tweets={tweets} />
      </section>

      <div ref={loadMoreRef} className='h-12'>
        {isFetchingNextPage && (
          <p className='py-4 text-center text-sm text-muted-foreground'>
            Daha fazla tweet yükleniyor...
          </p>
        )}
      </div>
    </div>
  )
}
