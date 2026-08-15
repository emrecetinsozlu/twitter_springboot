import { formatTweetDate } from "@/lib/utils"
import { useAuthStore } from "@/store/authStore"
import {Button} from "@/components/ui/button"
import { Trash2 } from "lucide-react"
import { useDeleteComment } from "@/hooks/useComments"
export function CommentCard({
  comment
}) {


  const user = useAuthStore((state) => state.user)

  const username = comment?.username || "unknown"
  const content = comment?.content || "Yorum içeriği burada görünecek."
  const createdAt = comment?.createdAt || "Tarih"

  const deleteCommentMutation = useDeleteComment(comment.tweetId)

  function handleDeleteComment(){
   
    deleteCommentMutation.mutate(comment.id)
  }


  return (
    <article className="border-b px-4 py-4">
      <div className="flex items-center gap-2">
        <span className="font-semibold">@{username}</span>
        <span className="text-muted-foreground">·</span>
        <span className="text-sm text-muted-foreground">{formatTweetDate(createdAt)}</span>
        {user?.id === comment?.userId && (
         
            <Button 
            variant="ghost" 
            size="icon" 
            onClick={handleDeleteComment}
            className="ml-auto"
            >
              <Trash2 className="h-4 w-4" />
            </Button>
          
          
        )}
      </div>

      <p className="mt-2 whitespace-pre-wrap text-sm leading-6">
        {content}
      </p>
    </article>
  )
}
