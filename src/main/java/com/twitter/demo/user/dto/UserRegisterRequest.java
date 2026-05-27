package com.twitter.demo.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
        @NotBlank
        @Size(min = 3, max=30)
        String username,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Size(min = 6,message = "password en az 6 karakter olmalı")
        String password
) {
}
