package com.fintech.deposit;

import java.math.BigDecimal;

public record DepositResponse(
        BigDecimal depositAmount,
        String userId
) {
}