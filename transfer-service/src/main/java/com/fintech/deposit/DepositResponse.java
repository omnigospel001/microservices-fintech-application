package com.fintech.deposit;

import java.math.BigDecimal;

public record DepositResponse(
        Integer id,
        BigDecimal depositAmount,
        String userId
) {
}