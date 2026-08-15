import { useInfiniteQuery } from "@tanstack/react-query"
import { getTweets } from "@/services/tweetServices"

export function useInfiniteTweets() {
  return useInfiniteQuery({
    queryKey: ["tweets"],

    queryFn: ({ pageParam }) => {
      return getTweets({
        page: pageParam,
        size: 10,
      })
    },

    initialPageParam: 0,

    getNextPageParam: (lastPage) => {
      if (lastPage.last) {
        return undefined
      }

      return lastPage.pageNumber + 1
    },

    refetchInterval: 15_000,
    refetchIntervalInBackground: false,
  })
}