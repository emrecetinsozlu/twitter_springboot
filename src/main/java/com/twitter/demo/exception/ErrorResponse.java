package com.twitter.demo.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        int status,
        String path,
        LocalDateTime HataZamani
) {
}
