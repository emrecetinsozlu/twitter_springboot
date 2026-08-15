import { useBookmarks } from "@/hooks/useBookmarks"
import { TweetList } from "@/components/TweetList"

const BookmarkPage = () => {
  const { data: bookmarks, isLoading, isError, error } = useBookmarks()

  if (isLoading) return <div>Loading...</div>
  if (isError) return <div>Error: {error.message}</div>

    return (
      <div>
        <TweetList tweets={bookmarks} />
      </div>
    )
  }


export default BookmarkPage
