import { useEffect } from "react"
import { Send } from "lucide-react"

import { Button } from "@/components/ui/button"
import { cn } from "@/lib/utils"
import { useState } from "react"
import { commentSchema } from "@/schemas/commentSchema"
import { useCreateComment } from "@/hooks/useComments"

export function TweetDetailCommentInput({ attentionKey, inputRef, tweetId }) {


  const createCommentMutation = useCreateComment(tweetId)

  const [content, setContent] = useState("")
  const [error, setError] = useState("")

  function handleSubmit(e) {
    e.preventDefault()

    const result = commentSchema.safeParse({ content })

    if (!result.success) {
      setError(result.error.issues[0].message)
      return
    }

    setError("")

    createCommentMutation.mutate(
      {
        tweetId,
        content: result.data.content,
      },
      {
        onSuccess: () => {
          setContent("")
        },
      }
    )
  }


  useEffect(() => {
    if (!attentionKey) return

    inputRef.current?.focus()
  }, [attentionKey, inputRef])

  return (
    <div
      key={attentionKey}
      className={cn(
        "sticky bottom-0 z-20 border-t border-border bg-background/95 p-3 backdrop-blur-sm transition-all",
        attentionKey && "animate-[pulse_900ms_ease-out_1]"
      )}
    >
      <form onSubmit={handleSubmit} className="flex items-center gap-2 rounded-md border border-input bg-background px-3 py-2 transition-shadow focus-within:ring-3 focus-within:ring-ring/50">
        <input
          ref={inputRef}
          type="text"
          placeholder="yorum yaz"
          className="min-w-0 flex-1 bg-transparent text-sm outline-none placeholder:text-muted-foreground"
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />

        <Button type="submit" size="sm" className="gap-1.5">
          <Send className="size-4 !fill-blue-500 !stroke-blue-500 [&_*]:!fill-blue-500 [&_*]:!stroke-blue-500" />
          Gönder
        </Button>
        
      </form>
      {error && <p className="mt-2 text-xs text-red-500">{error}</p>}
    </div>
  )
}
