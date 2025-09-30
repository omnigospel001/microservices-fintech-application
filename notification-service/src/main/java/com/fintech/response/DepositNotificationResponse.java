package com.fintech.response;

import java.math.BigDecimal;

public record DepositNotificationResponse(
        String firstName,
        String lastName,
        String userEmail,
        Long accountNumber,
        BigDecimal depositAmount
) {
}
