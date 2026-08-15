package com.twitter.demo.media;


import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;



/*
MultipartFile al
↓
Cloudinary'ye yükle
↓
secure_url dön
 */

@Service
@RequiredArgsConstructor
public class ImageUploadService {
    private final Cloudinary cloudinary;

    public ImageUploadResult uploadImage(MultipartFile file) {
        try {

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of(
                            "folder", "twitter-clone/tweets",
                            "resource_type", "image",
                            "transformation", "w_1200,c_limit,q_auto:good,f_auto"
                    )
            );
            String imageUrl = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();

            return new ImageUploadResult(imageUrl, publicId);

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed");
        }
    }

}
