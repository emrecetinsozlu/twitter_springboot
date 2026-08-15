import { useMutation, useQueryClient } from "@tanstack/react-query"
import {
  addBookmark,
  removeBookmark,
} from "@/services/bookmarkService"

export function useBookmarkMutations() {
  const queryClient = useQueryClient()

  const addBookmarkMutation = useMutation({
    mutationFn: addBookmark,

    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["bookmarks"],
      })

      queryClient.invalidateQueries({
        queryKey: ["tweets"],
      })
    },
  })

  const removeBookmarkMutation = useMutation({
    mutationFn: removeBookmark,

    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["bookmarks"],
      })

      queryClient.invalidateQueries({
        queryKey: ["tweets"],
      })
    },
  })

  return {
    addBookmarkMutation,
    removeBookmarkMutation,
  }
}