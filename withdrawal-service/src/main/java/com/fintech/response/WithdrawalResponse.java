package com.fintech.response;

import java.math.BigDecimal;

public record WithdrawalResponse(
        BigDecimal withdrawalAmount,
        String userId
) {
}
