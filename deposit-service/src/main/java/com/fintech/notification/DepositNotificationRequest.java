package com.fintech.notification;

import java.math.BigDecimal;

public record DepositNotificationRequest(
        String firstName,
        String lastName,
        String userEmail,
        Long accountNumber,
        BigDecimal depositAmount
) {
}
