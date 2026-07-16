package com.fintech.consumer;

import com.fintech.enums.SettlementStatus;
import com.fintech.enums.TransactionType;
import com.fintech.notification.DepositNotificationRequest;
import com.fintech.notification.TransferNotificationRequest;
import com.fintech.notification.WithdrawalNotificationRequest;
import com.fintech.service.SettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class SettlementConsumer {

    private final SettlementService settlementService;

    @KafkaListener(topics = "deposit-topic", groupId = "settlement-deposit-group")
    public void consumeDeposit(DepositNotificationRequest depositNotificationRequest) {
        log.info(format("Consuming the message from deposit-topic for settlement:: %s", depositNotificationRequest));
        settlementService.createSettlement(
                TransactionType.DEPOSIT,
                generateReferenceId(TransactionType.DEPOSIT, depositNotificationRequest.accountNumber(), depositNotificationResponseHash(depositNotificationRequest)),
                null,
                depositNotificationRequest.accountNumber().toString(),
                depositNotificationRequest.depositAmount(),
                SettlementStatus.SETTLED
        );
    }

    @KafkaListener(topics = "withdrawal-topic", groupId = "settlement-withdrawal-group")
    public void consumeWithdrawal(WithdrawalNotificationRequest withdrawalNotificationRequest) {
        log.info(format("Consuming the message from withdrawal-topic for settlement:: %s", withdrawalNotificationRequest));
        settlementService.createSettlement(
                TransactionType.WITHDRAWAL,
                generateReferenceId(TransactionType.WITHDRAWAL, withdrawalNotificationRequest.accountNumber(), withdrawalNotificationRequest.hashCode()),
                null,
                withdrawalNotificationRequest.accountNumber().toString(),
                withdrawalNotificationRequest.withdrawalAmount(),
                SettlementStatus.PENDING
        );
    }

    @KafkaListener(topics = "transfer-topic", groupId = "settlement-transfer-group")
    public void consumeTransfer(TransferNotificationRequest transferNotificationRequest) {
        log.info(format("Consuming the message from transfer-topic for settlement:: %s", transferNotificationRequest));
        settlementService.createSettlement(
                TransactionType.TRANSFER,
                generateReferenceId(TransactionType.TRANSFER, transferNotificationRequest.senderAccountNumber(), transferNotificationRequest.hashCode()),
                null,
                transferNotificationRequest.senderAccountNumber().toString(),
                transferNotificationRequest.transferAmount(),
                SettlementStatus.PENDING
        );
    }

    private String generateReferenceId(TransactionType type, Long accountNumber, int hash) {
        return type.name() + "-" + accountNumber + "-" + Math.abs(hash);
    }

    private int depositNotificationResponseHash(DepositNotificationRequest req) {
        return req.hashCode();
    }

}
