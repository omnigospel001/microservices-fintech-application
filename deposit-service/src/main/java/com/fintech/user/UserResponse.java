package com.fintech.user;

public record UserResponse(
        Integer id,
        String firstName,
        String lastName,
        String email,
        Long accountNumber
) {
}
