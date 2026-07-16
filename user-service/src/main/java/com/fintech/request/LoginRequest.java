package com.fintech.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "User email is required")
        @Email(message = "User Email is not a valid email address")
        String email,
        @NotNull(message = "User password is required")
        String password
) {
}
