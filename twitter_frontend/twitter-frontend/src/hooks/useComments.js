// src/hooks/useComments.js
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import {
  createComment,
  deleteComment,
  getCommentsByTweetId,
} from "@/services/commentService"

export function useComments(tweetId) {
  return useQuery({
    queryKey: ["comments", tweetId],
    queryFn: () => getCommentsByTweetId(tweetId),
    enabled: !!tweetId,
   
    refetchInterval: 10_000,
    refetchOnWindowFocus: true
  })
}

export function useCreateComment(tweetId) {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: createComment,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["comments", tweetId],
      })
    },
  })
}

export function useDeleteComment(tweetId) {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: deleteComment,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["comments", tweetId],
      })
    },
  })
}