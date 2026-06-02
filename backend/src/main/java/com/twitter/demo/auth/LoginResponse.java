package com.twitter.demo.auth;

public record LoginResponse(
        Long userId,
        String username,
        String message
        // eskiden login successful message dönüyorduk artık token döneceğiz ama bunu authresult dto da yapalım
) {
}
