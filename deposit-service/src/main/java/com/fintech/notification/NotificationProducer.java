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

    private final KafkaTemplate<String, DepositNotificationRequest> kafkaTemplate;

    public void sendNotification(DepositNotificationRequest notificationRequest) {
        log.info("Sending notification with body = : {} :", notificationRequest);
        Message<DepositNotificationRequest> message = MessageBuilder
                .withPayload(notificationRequest)
                .setHeader(TOPIC, "deposit-topic")
                .build();

        kafkaTemplate.send(message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Unable to send message to deposit-topic: {}", ex.getMessage(), ex);
                    } else {
                        log.info("Message sent to deposit-topic with offset {}", result.getRecordMetadata().offset());
                    }
                });
    }

}
