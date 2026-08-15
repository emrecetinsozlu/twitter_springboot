import { useRef, useState } from "react"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import { ImageIcon, X } from "lucide-react"

import { Button } from "@/components/ui/button"
import { createTweetSchema } from "@/schemas/tweetSchema"
import { useCreateTweet } from "@/hooks/useCreateTweet"

export function TweetComposer({ isAuthenticated }) {
  const createTweetMutation = useCreateTweet()
  const imageInputRef = useRef(null)
  const [imagePreviewUrl, setImagePreviewUrl] = useState("")

  const {
    register,
    handleSubmit,
    reset,
    watch,
    setValue,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(createTweetSchema),
    defaultValues: {
      content: "",
      image: undefined,
    },
  })

  const content = watch("content") || ""
  const selectedImage = watch("image")
  const characterCount = content.length
  const canSubmit = content.trim().length > 0 || Boolean(selectedImage)
  const isFormDisabled = !isAuthenticated || createTweetMutation.isPending

  function onSubmit(data) {
    const formData = new FormData()

    formData.append("content", data.content)

    if (data.image) {
      formData.append("image", data.image)
    }

    createTweetMutation.mutate(formData, {
      onSuccess: () => {
        reset()
        clearImage()
      },
    })
  }

  function selectImage(event) {
    const file = event.target.files?.[0]

    if (!file) {
      clearImage()
      return
    }

    setValue("image", file, {
      shouldDirty: true,
      shouldValidate: true,
    })
    setImagePreviewUrl(URL.createObjectURL(file))
  }

  function clearImage() {
    setValue("image", undefined, {
      shouldDirty: true,
      shouldValidate: true,
    })

    if (imageInputRef.current) {
      imageInputRef.current.value = ""
    }

    setImagePreviewUrl("")
  }

  function openImagePicker() {
    if (!isFormDisabled) {
      imageInputRef.current?.click()
    }
  }

  return (
    <section className="border-b border-border p-4">
      <form onSubmit={handleSubmit(onSubmit)} encType="multipart/form-data">
        <textarea
          {...register("content")}
          placeholder="Neler oluyor?"
          rows={3}
          maxLength={280}
          disabled={isFormDisabled}
          className="w-full resize-none bg-transparent text-sm outline-none placeholder:text-muted-foreground disabled:cursor-not-allowed disabled:opacity-50"
        />

        {errors.content && (
          <p className="mt-1 text-sm text-destructive">
            {errors.content.message}
          </p>
        )}

        <input
          ref={imageInputRef}
          type="file"
          accept="image/jpeg,image/png,image/webp,image/gif"
          disabled={isFormDisabled}
          onChange={selectImage}
          className="sr-only"
        />

        {imagePreviewUrl && (
          <div className="relative mt-3 overflow-hidden rounded-md border border-border">
            <img
              src={imagePreviewUrl}
              alt={selectedImage?.name || "Seçilen görsel"}
              className="max-h-70 w-full object-cover"
            />

            <Button
              type="button"
              variant="secondary"
              size="icon-sm"
              onClick={clearImage}
              disabled={isFormDisabled}
              className="absolute right-2 top-2 bg-background/90 hover:bg-background"
              aria-label="Görseli kaldır"
            >
              <X className="h-4 w-4" />
            </Button>
          </div>
        )}

        {errors.image && (
          <p className="mt-1 text-sm text-destructive">
            {errors.image.message}
          </p>
        )}

        <div className="mt-3 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <button
              type="button"
              onClick={openImagePicker}
              disabled={isFormDisabled}
              className="inline-flex h-8 w-8 items-center justify-center rounded-md text-muted-foreground transition-colors hover:bg-muted hover:text-foreground disabled:pointer-events-none disabled:opacity-50"
              aria-label="Görsel ekle"
              title="Görsel ekle"
            >
              <ImageIcon className="h-8 w-8" />
            </button>

            <span className="text-xs text-muted-foreground">
              {characterCount}/280
            </span>
          </div>

          <Button
            type="submit"
            size="sm"
            disabled={isFormDisabled || !canSubmit}
          >
            {createTweetMutation.isPending ? "Gönderiliyor..." : "Gönder"}
          </Button>
        </div>
      </form>
    </section>
  )
}
