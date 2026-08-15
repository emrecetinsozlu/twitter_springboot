package com.twitter.demo.media;

public record ImageUploadResult(
        String imageUrl,
        String publicId
) {
}
