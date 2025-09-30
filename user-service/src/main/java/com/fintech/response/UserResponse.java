package com.fintech.response;

public record UserResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        Long accountNumber
) {

}
