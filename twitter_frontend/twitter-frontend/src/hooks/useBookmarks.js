import { useQuery } from "@tanstack/react-query"
import { getMyBookmarks } from "@/services/bookmarkService"

export function useBookmarks() {
  return useQuery({
    queryKey: ["bookmarks"],
    queryFn: getMyBookmarks,
  })
}