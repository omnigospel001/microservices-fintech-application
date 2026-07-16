package com.fintech.consumer;


import com.fintech.response.DepositNotificationResponse;
import com.fintech.response.SettlementNotificationResponse;
import com.fintech.response.TransferNotificationResponse;
import com.fintech.response.WithdrawalNotificationResponse;
import com.fintech.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "deposit-topic", groupId = "deposit-email-group")
    public void consumeDepositEmailNotification(DepositNotificationResponse depositNotificationResponse) {
        log.info(format("Consuming the message from deposit-topic Topic:: %s", depositNotificationResponse));
        //
        emailService.sendDepositEmailNotification(depositNotificationResponse);
    }

    @KafkaListener(topics = "withdrawal-topic", groupId = "withdrawal-email-group")
    public void consumeWithdrawalEmailNotification(WithdrawalNotificationResponse withdrawalNotificationResponse) {
        log.info(format("Consuming the message from withdrawal-topic Topic:: %s", withdrawalNotificationResponse));
        //
        emailService.sendWithdrawalEmailNotification(withdrawalNotificationResponse);
    }

    @KafkaListener(topics = "transfer-topic", groupId = "transfer-receiver-group")
    public void sendTransferEmailNotificationToReceiver(TransferNotificationResponse transferNotificationResponse) {
        log.info(format("Consuming the message from transfer-topic Topic:: %s", transferNotificationResponse));
        //
        emailService.sendTransferEmailNotificationToReceiver(transferNotificationResponse);
    }

    @KafkaListener(topics = "transfer-topic", groupId = "transfer-sender-group")
    public void sendTransferEmailNotificationToSender(TransferNotificationResponse transferNotificationResponse) {
        log.info(format("Consuming the message from transfer-topic Topic:: %s", transferNotificationResponse));
        //
        emailService.sendTransferEmailNotificationToSender(transferNotificationResponse);
    }

    @KafkaListener(topics = "settlement-topic", groupId = "settlement-email-group")
    public void sendSettlementEmailNotification(SettlementNotificationResponse settlementNotificationResponse) {
        log.info(format("Consuming the message from settlement-topic Topic:: %s", settlementNotificationResponse));
        //
        emailService.sendSettlementEmailNotification(settlementNotificationResponse);
    }

}
