package com.fintech.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserRequest(

        @NotNull(message = "User firstname is required")
        String firstname,
        @NotNull(message = "User lastname is required")
        String lastname,
        @NotNull(message = "User Email is required")
        @Email(message = "User Email is not a valid email address")
        String email,
        @NotNull(message = "User password is required")
        String password
    ) {

    }

