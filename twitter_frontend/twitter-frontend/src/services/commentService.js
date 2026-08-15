// src/services/commentService.js
import { api } from "@/services/api"

export async function getCommentsByTweetId(tweetId) {
  const response = await api.get(`/comments/tweet/${tweetId}`)
  return response.data
}

export async function createComment({ tweetId, content }) {
  const response = await api.post("/comments", {
    tweetId,
    content,
  })

  return response.data
}

export async function deleteComment(commentId) {
  await api.delete(`/comments/${commentId}`)
}