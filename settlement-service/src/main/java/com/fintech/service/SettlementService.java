package com.fintech.service;

import com.fintech.entity.Settlement;
import com.fintech.enums.SettlementStatus;
import com.fintech.enums.TransactionType;
import com.fintech.mapper.Mapper;
import com.fintech.notification.NotificationProducer;
import com.fintech.notification.SettlementNotificationRequest;
import com.fintech.repository.SettlementRepo;
import com.fintech.response.SettlementResponse;
import com.fintech.user.UserClient;
import com.fintech.user.UserResponse;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SettlementService {

    private final SettlementRepo settlementRepo;
    private final UserClient userClient;
    private final Mapper mapper;
    private final NotificationProducer notificationProducer;

    public Settlement createSettlement(TransactionType transactionType, String referenceId,
                                       String userIdInput, String accountNumberInput,
                                       BigDecimal amount, SettlementStatus status) {
        String userId = userIdInput != null ? userIdInput : accountNumberInput;

        Settlement settlement = Settlement.builder()
                .transactionType(transactionType)
                .referenceId(referenceId)
                .userId(userId)
                .amount(amount)
                .status(status)
                .build();

        settlementRepo.save(settlement);
        log.info("Settlement created: {}", settlement);

        if (status == SettlementStatus.SETTLED) {
            sendSettlementNotification(settlement);
        }

        return settlement;
    }

    public SettlementResponse findById(Integer id) {
        return settlementRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Settlement not found with id: " + id));
    }

    public List<SettlementResponse> findByUserId(String userId) {
        return settlementRepo.findByUserId(userId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<SettlementResponse> findByStatus(SettlementStatus status) {
        return settlementRepo.findByStatus(status).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<SettlementResponse> findAll() {
        return settlementRepo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public SettlementResponse processSettlement(Integer id) {
        Settlement settlement = settlementRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Settlement not found with id: " + id));

        if (settlement.getStatus() != SettlementStatus.PENDING) {
            throw new RuntimeException("Only pending settlements can be processed");
        }

        settlement.setStatus(SettlementStatus.SETTLED);
        settlement.setSettledAt(LocalDateTime.now());
        settlementRepo.save(settlement);

        sendSettlementNotification(settlement);
        log.info("Settlement processed: {}", settlement);

        return mapper.toResponse(settlement);
    }

    public int batchProcessPendingSettlements() {
        List<Settlement> pending = settlementRepo.findByStatus(SettlementStatus.PENDING);
        int processed = 0;

        for (Settlement settlement : pending) {
            settlement.setStatus(SettlementStatus.SETTLED);
            settlement.setSettledAt(LocalDateTime.now());
            settlementRepo.save(settlement);
            sendSettlementNotification(settlement);
            processed++;
        }

        log.info("Batch processed {} pending settlements", processed);
        return processed;
    }

    private void sendSettlementNotification(Settlement settlement) {
        try {
            var userOpt = userClient.findByUserId(settlement.getUserId());
            UserResponse user = userOpt.orElse(null);

            String firstName = user != null ? user.firstName() : "Valued";
            String lastName = user != null ? user.lastName() : "Customer";
            String email = user != null ? user.email() : "";
            Long accountNumber = user != null ? user.accountNumber() : null;

            notificationProducer.sendNotification(
                    new SettlementNotificationRequest(
                            firstName,
                            lastName,
                            email,
                            accountNumber,
                            settlement.getTransactionType(),
                            settlement.getReferenceId(),
                            settlement.getAmount(),
                            settlement.getStatus(),
                            settlement.getSettledAt()
                    )
            );
        } catch (Exception e) {
            log.error("Failed to send settlement notification for settlement id {}: {}", settlement.getId(), e.getMessage());
        }
    }

}
