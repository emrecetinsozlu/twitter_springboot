import { useQuery } from "@tanstack/react-query"
import { getTweetById } from "@/services/tweetServices"

export function useTweetById(tweetId) {
  return useQuery({
    queryKey: ["tweet", tweetId],
    queryFn: () => getTweetById(tweetId),
    enabled: !!tweetId,
  })
}

