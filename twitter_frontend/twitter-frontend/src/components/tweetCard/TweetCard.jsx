import { Heart, MessageCircle, Repeat2, Bookmark } from "lucide-react"
import { useNavigate } from "react-router-dom"

import {useBookmarkMutations} from "@/hooks/useBookmarkMutations"
import { useTweetLike } from "@/hooks/useLikeMutations"



function formatTweetDate(value) {
  if (!value) return ""

  const date = new Date(value)

  if (Number.isNaN(date.getTime())) {
    return value
  }

  return new Intl.DateTimeFormat("tr-TR", {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(date)
}

export function TweetCard({ tweet }) {
  const navigate = useNavigate()
  const username = tweet?.username || "unknown"
  const displayName = tweet?.displayName || username
  const content = tweet?.content || ""
  const tweetImage = tweet?.imageUrl || tweet?.image || tweet?.imagePath

  //console.log(tweet)

  const { addBookmarkMutation, removeBookmarkMutation } = useBookmarkMutations()
  const { likeMutation, dislikeMutation } = useTweetLike()


  const handleCardClick = () => {
    if (!tweet?.id) return

    navigate(`/tweets/${tweet.id}`, {
      state: { tweet },
    })
  }

  const handleCardKeyDown = (event) => {
    if (event.key === "Enter" || event.key === " ") {
      event.preventDefault()
      handleCardClick()
    }
  }

  const handleBookmark = () => {
    if(tweet.bookmarked){
      removeBookmarkMutation.mutate(tweet.id)
    } else {
      addBookmarkMutation.mutate(tweet.id)
    }
  }

  const handleLike = () => {
    if(tweet.liked){
      dislikeMutation.mutate(tweet.id)
    } else {
      likeMutation.mutate(tweet.id)
    }
  }


  const isBookmarkPending = addBookmarkMutation.isPending || removeBookmarkMutation.isPending
  const isLikePending = likeMutation.isPending || dislikeMutation.isPending

  return (
    <article
      className="cursor-pointer border-b px-4 py-4 hover:bg-muted/40 transition-colors"
      onClick={handleCardClick}
      onKeyDown={handleCardKeyDown}
      role="button"
      tabIndex={0}
    >
      <div className="flex gap-3">
        <div className="h-10 w-10 flex-shrink-0 rounded-full bg-muted flex items-center justify-center font-semibold">
          {username?.charAt(0)?.toUpperCase() || "U"}
        </div>
       
        <div className="min-w-0 flex-1">
          <div className="flex items-center gap-2">
            <span className="font-semibold">
              {displayName}
            </span>

            <span className="text-sm text-muted-foreground">
              @{username}
            </span>

            {tweet?.createdAt && (
              <>
                <span className="text-muted-foreground">·</span>
                <span className="text-sm text-muted-foreground">
                  {formatTweetDate(tweet.createdAt)}
                </span>
              </>
            )}
          </div>

          <p className="mt-2 whitespace-pre-wrap text-sm leading-6">
            {content}
          </p>

          {tweet?.hashtags?.length > 0 && (
            <div className="mt-2 flex flex-wrap gap-2">
              {tweet.hashtags.map((hashtag) => (
                <span
                  key={hashtag}
                  className="text-sm text-blue-500"
                >
                  #{hashtag}
                </span>
              ))}
            </div>
          )}

          {tweetImage && (
            <div className="mt-3 overflow-hidden rounded-md border border-border">
              <img
                src={tweetImage}
                alt={`${displayName} tweet görseli`}
                className="max-h-96 w-full object-cover"
              />
            </div>
          )}

          <div className="mt-4 flex w-full items-center justify-around text-muted-foreground">
            <button
              className="flex items-center gap-2 text-sm"
              onClick={(event) => event.stopPropagation()}
            >
              <MessageCircle className="h-4 w-4" />
              <span>{tweet?.commentCount ?? 0}</span>
            </button>

            <button
              className="flex items-center gap-2 text-sm"
              onClick={(event) => event.stopPropagation()}
            >
              <Repeat2 className="h-4 w-4" />
              <span>{tweet?.retweetCount ?? 0}</span>
            </button>

            <button className="flex items-center gap-2 text-sm"
            onClick={(event) => {
              event.stopPropagation()
              handleLike()
            }}
            disabled={isLikePending}
            >
              <Heart className={tweet.liked ? "h-4 w-4 fill-red-700 text-red-700" : "h-4 w-4 fill-none text-red-500"} />
              <span>{tweet?.likeCount ?? 0}</span>
            </button>

            <button className="flex items-center gap-2 text-sm hover:translate-y-[-2px] transition-transform"
            onClick={(event) => {
              event.stopPropagation()
              handleBookmark()
            }}
            disabled={isBookmarkPending}
            >
              <Bookmark className={
                  tweet.bookmarked
                    ? "h-4 w-4 fill-black text-black"
                    : "h-4 w-4 fill-none text-red-500"
                    } />
            </button>
          </div>
        </div>
       
        
      </div>
    </article>
  )
}
