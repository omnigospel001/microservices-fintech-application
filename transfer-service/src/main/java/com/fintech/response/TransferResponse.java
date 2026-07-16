package com.fintech.response;

import java.math.BigDecimal;

public record TransferResponse(
        Long receiverAccountNumber,
        BigDecimal transferAmount,
        String userId
) {
}
