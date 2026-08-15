package com.twitter.demo.media;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestUploadController {

    private final ImageUploadService imageUploadService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> uploadImage(@RequestParam("image") MultipartFile image) {
        ImageUploadResult imageUrl = imageUploadService.uploadImage(image);

        return Map.of(
                "imageUrl", imageUrl.imageUrl(),
                "publicId", imageUrl.publicId()
        );
    }
}