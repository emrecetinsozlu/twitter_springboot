import { z } from "zod"

export const commentSchema = z.object({
    content: z
      .string()
      .trim()
      .min(1, "Yorum boş olamaz.")
      .max(280, "Yorum en fazla 280 karakter olabilir."),
  })