package com.fintech.user;

public record SenderResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        Long accountNumber
) {
}