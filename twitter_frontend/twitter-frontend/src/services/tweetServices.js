import { api } from "@/services/api"

export async function getTweets({ page = 0, size = 10 }) {
  const response = await api.get("/tweet", {
    params: {
      page,
      size,
    },
  })

  return response.data
}

export async function getTweetById(tweetId) {
  const response = await api.get(`/tweet/${tweetId}`)
  return response.data
}

export async function createTweet(tweetData) {
  const response = await api.post("/tweet", tweetData)

  return response.data
}