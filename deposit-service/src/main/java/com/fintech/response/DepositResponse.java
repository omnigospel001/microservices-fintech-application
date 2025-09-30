package com.fintech.response;

import java.math.BigDecimal;

public record DepositResponse(
        BigDecimal depositAmount,
        String userId
) {
}
