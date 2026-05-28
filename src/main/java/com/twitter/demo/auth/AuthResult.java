package com.twitter.demo.auth;

public record AuthResult(
        Long userId,
        String username,
        String token
) {
}
