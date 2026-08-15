import { api } from "@/services/api"

export const likeTweet = async (tweetId) => {
    const response = await api.post("/like", {
      tweetId,
    });
  
    return response.data;
  };

  export const dislikeTweet = async (tweetId) => {
    const response = await api.post("/dislike", {
      tweetId,
    });
  
    return response.data;
  };