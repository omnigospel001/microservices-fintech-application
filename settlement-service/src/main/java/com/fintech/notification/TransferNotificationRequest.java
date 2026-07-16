package com.fintech.notification;

import java.math.BigDecimal;

public record TransferNotificationRequest(
        String senderFirstName,
        String senderLastName,
        String senderEmail,
        Long senderAccountNumber,
        String receiverFirstName,
        String receiverLastName,
        String receiverEmail,
        Long receiverAccountNumber,
        BigDecimal transferAmount
) {
}
