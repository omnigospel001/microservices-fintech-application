package com.fintech.response;

import com.fintech.enums.SettlementStatus;
import com.fintech.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SettlementResponse(
        Integer id,
        TransactionType transactionType,
        String referenceId,
        String userId,
        BigDecimal amount,
        SettlementStatus status,
        LocalDateTime createdAt,
        LocalDateTime settledAt
) {
}
