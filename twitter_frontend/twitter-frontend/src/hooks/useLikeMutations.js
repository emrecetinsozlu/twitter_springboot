import { useMutation } from "@tanstack/react-query";
import { likeTweet, dislikeTweet } from "@/services/likeService";
import { useQueryClient } from "@tanstack/react-query";

export function useTweetLike() {
    const queryClient = useQueryClient();
  
    const likeMutation = useMutation({
      mutationFn: likeTweet,
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["tweets"],
        });
      },
    });
  
    const dislikeMutation = useMutation({
      mutationFn: dislikeTweet,
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["tweets"],
        });
      },
    });
  
    return {
      likeMutation,
      dislikeMutation
    };
  }