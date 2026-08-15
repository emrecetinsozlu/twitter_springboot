import { z } from "zod"

const MAX_TWEET_LENGTH = 280
const MAX_IMAGE_SIZE = 5 * 1024 * 1024
const ACCEPTED_IMAGE_TYPES = ["image/jpeg", "image/png", "image/webp", "image/gif"]


const imageSchema = z
  .instanceof(File)
  .optional()
  .refine(
    (file) => !file || file.size <= MAX_IMAGE_SIZE,
    "Görsel en fazla 5 MB olabilir."
  )
  .refine(
    (file) => !file || ACCEPTED_IMAGE_TYPES.includes(file.type),
    "Sadece JPG, PNG, WebP veya GIF formatında görsel ekleyebilirsin."
  )


export const createTweetSchema = z.object({
  content: z
    .string()
    .trim()
    .max(MAX_TWEET_LENGTH, "Tweet en fazla 280 karakter olabilir."),
  image: imageSchema,
}).refine(
  ({ content, image }) => content.length > 0 || Boolean(image),
  {
    message: "Tweet içeriği yazmalı veya bir görsel eklemelisin.",
    path: ["content"],
  }
)
