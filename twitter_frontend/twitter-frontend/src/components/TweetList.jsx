import { TweetCard } from "@/components/tweetCard/TweetCard"

export function TweetList({ tweets }) {
  if (!tweets.length) {
    return (
      <div className="px-4 py-8 text-center text-muted-foreground">
        Henüz gönderi yok. Akış burada görünecek.
      </div>
    )
  }

  return (
    <div>
      {tweets.map((tweet) => (
        <TweetCard key={tweet.id} tweet={tweet} />
      ))}
    </div>
  )
}