package com.fintech.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    private final KafkaTemplate<String, WithdrawalNotificationRequest> kafkaTemplate;

    public void sendNotification(WithdrawalNotificationRequest notificationRequest) {
        log.info("Sending notification with body = : {} :", notificationRequest);
        Message<WithdrawalNotificationRequest> message = MessageBuilder
                .withPayload(notificationRequest)
                .setHeader(TOPIC, "withdrawal-topic")
                .build();

        kafkaTemplate.send(message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Unable to send message to withdrawal-topic: {}", ex.getMessage(), ex);
                    } else {
                        log.info("Message sent to withdrawal-topic with offset {}", result.getRecordMetadata().offset());
                    }
                });
    }
}
