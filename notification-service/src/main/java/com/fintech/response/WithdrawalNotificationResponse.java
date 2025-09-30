package com.fintech.response;

import java.math.BigDecimal;

public record WithdrawalNotificationResponse(
        String firstName,
        String lastName,
        String userEmail,
        Long accountNumber,
        BigDecimal withdrawalAmount
) {
}
