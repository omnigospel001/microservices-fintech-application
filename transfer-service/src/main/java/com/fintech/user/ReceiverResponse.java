package com.fintech.user;

public record ReceiverResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        Long accountNumber
) {
}
