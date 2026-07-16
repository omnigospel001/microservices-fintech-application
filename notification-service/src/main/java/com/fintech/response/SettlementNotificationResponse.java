package com.fintech.response;

import com.fintech.enums.SettlementStatus;
import com.fintech.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SettlementNotificationResponse(
        String firstName,
        String lastName,
        String email,
        Long accountNumber,
        TransactionType transactionType,
        String referenceId,
        BigDecimal amount,
        SettlementStatus status,
        LocalDateTime settledAt
) {
}
