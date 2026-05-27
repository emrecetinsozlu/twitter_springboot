package com.twitter.demo.auth;

public record LoginResponse(
        Long userId,
        String username,
        String message
) {
}
