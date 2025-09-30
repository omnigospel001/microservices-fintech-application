package com.fintech.user;

public record UserResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        Long accountNumber
) {
}