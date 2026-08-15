import { api } from "@/services/api"



export async function getMyBookmarks() {
    const response = await api.get("/bookmarks")
    return response.data
  }


  export async function addBookmark(tweetId) {
    const response = await api.post(`/bookmarks/${tweetId}`)
    return response.data
  }
  
  export async function removeBookmark(tweetId) {
    const response = await api.delete(`/bookmarks/${tweetId}`)
    return response.data
  }