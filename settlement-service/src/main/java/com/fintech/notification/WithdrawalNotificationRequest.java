package com.fintech.notification;

import java.math.BigDecimal;

public record WithdrawalNotificationRequest(
        String firstName,
        String lastName,
        String userEmail,
        Long accountNumber,
        BigDecimal withdrawalAmount
) {
}
