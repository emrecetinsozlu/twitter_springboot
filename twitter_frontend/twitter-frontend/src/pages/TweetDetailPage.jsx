import { useRef, useState } from "react"
import { useLocation, useParams } from "react-router-dom"

import { CommentCard } from "@/components/commentCard/CommentCard"
import { TweetDetailCommentInput } from "@/components/tweetDetailCommentInput/TweetDetailCommentInput"
import { TweetCard } from "@/components/tweetCard/TweetCard"

import { useComments } from "@/hooks/useComments"
import { useTweetById } from "@/hooks/useTweetById"



export default function TweetDetailPage() {
  const { tweetId } = useParams()
  const location = useLocation()
  const commentInputRef = useRef(null)
  const [commentInputAttentionKey, setCommentInputAttentionKey] = useState(0)

  const { data: comments } = useComments(tweetId)
  
 

 //Eğer yönlendirme ile geldiysek location.state içindeki tweet'i kullanıyoruz. Eğer gelmediysek (refresh veya direkt URL) state boş olduğu için URL'deki tweetId ile fetch edip fetchedTweet'i kullanıyoruz.
  const {
    data: fetchedTweet,
    isLoading,
    isError,
  } = useTweetById(tweetId)

  if (isLoading) return <div>Tweet yükleniyor...</div>
  if (isError) return <div>Tweet bulunamadı.</div>

  const tweet = location.state?.tweet ?? fetchedTweet



  function handleTweetActionClick(event) {
    const button = event.target.closest("button")

    if (!button || !event.currentTarget.contains(button)) return

    const actionButtons = Array.from(event.currentTarget.querySelectorAll("button"))

    if (actionButtons[0] !== button) return

    setCommentInputAttentionKey((currentKey) => currentKey + 1)
  }

  return (
    <div className="pb-4">
      <header className="sticky top-0 z-10 border-b border-border bg-background/80 px-4 py-3 backdrop-blur-sm">
        <h1 className="text-lg font-bold">Tweet</h1>
      </header>

      <div onClickCapture={handleTweetActionClick}>
        <TweetCard tweet={tweet} />
      </div>

      <section>
        <div className="border-b px-4 py-3">
          <h2 className="text-base font-bold">Yorumlar</h2>
        </div>

        <div>
          {comments?.map((comment) => (
            
            <CommentCard key={comment.id} comment={comment} />
          ))}
        </div>
      </section>
     
    
      <TweetDetailCommentInput
        attentionKey={commentInputAttentionKey}
          inputRef={commentInputRef}
          tweetId={tweetId}
        />
      
    </div>
  )
}



