package com.fintech.service;

import com.fintech.mapper.Mapper;
import com.fintech.notification.DepositNotificationRequest;
import com.fintech.notification.NotificationProducer;
import com.fintech.repository.DepositRepo;
import com.fintech.entity.Deposit;
import com.fintech.request.DepositRequest;
import com.fintech.user.UserClient;
import com.fintech.user.UserResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DepositService {

    private final DepositRepo depositRepo;
    private final UserClient userClient;
    private final Mapper mapper;
    private final NotificationProducer notificationProducer;
    private final EntityManager entityManager;

    public Deposit deposit(DepositRequest depositRequest) {
      var user = userClient.findByUserId(depositRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

      log.info("User info retrieved {}", user);

      var deposit = mapper.mapDeposit(depositRequest);
      depositRepo.save(deposit);

      notificationProducer.sendNotification(
              new DepositNotificationRequest(
                      user.firstName(),
                      user.lastName(),
                      user.email(),
                      user.accountNumber(),
                      deposit.getDepositAmount()
              )
      );

      return deposit;

    }

    public Deposit withdrawal(BigDecimal withdrawalAmount, Integer depositId, String userId) {

        var deposit = depositRepo.findById(depositId)
                .orElseThrow(() -> new EntityNotFoundException("User not found In Deposit"));

        var user = userClient.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        validateSufficientBalance(userId, withdrawalAmount);

       Query query = entityManager.createNativeQuery("UPDATE deposit SET deposit_amount = deposit_amount - :withdrawalAmount WHERE id = :depositId AND user_id = :userId");

       query.setParameter("withdrawalAmount", withdrawalAmount);
       query.setParameter("depositId", deposit.getId());
       query.setParameter("userId", user.id());

       query.executeUpdate();

       return deposit;

    }

    public Deposit debitForTransfer(BigDecimal transferAmount, Integer depositId, String userId) {

        var deposit = depositRepo.findById(depositId)
                .orElseThrow(() -> new EntityNotFoundException("User not found In Deposit"));

        var user = userClient.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        validateSufficientBalance(userId, transferAmount);

        Query query = entityManager.createNativeQuery("UPDATE deposit SET deposit_amount = deposit_amount - :transferAmount WHERE id = :depositId AND user_id = :userId");

        query.setParameter("transferAmount", transferAmount);
        query.setParameter("depositId", deposit.getId());
        query.setParameter("userId", user.id());

        query.executeUpdate();

        return deposit;

    }

    public void validateSufficientBalance(String userId, BigDecimal requestedAmount) {

        var user = userClient.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Query query = entityManager.createNativeQuery("SELECT COALESCE(SUM(deposit_amount), 0) FROM deposit WHERE user_id = :user_id");
        query.setParameter("user_id", user.id());

        BigDecimal totalAmount = (BigDecimal) query.getSingleResult();

        if (totalAmount.compareTo(requestedAmount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

    }

    public BigDecimal getBalance(String userId) {
        var user = userClient.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Query query = entityManager.createNativeQuery("SELECT COALESCE(SUM(deposit_amount), 0) FROM deposit WHERE user_id = :user_id");
        query.setParameter("user_id", user.id());

        return (BigDecimal) query.getSingleResult();
    }

    public UserResponse creditTheReceiver(Long receiverAccountNumber, BigDecimal transferAmount) {

        log.info("AccountNumber: {}", receiverAccountNumber);
        var user = userClient.findByAccountNumber(receiverAccountNumber)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        log.info("User AccountNumber: {}", receiverAccountNumber);


        Query insertIntoDeposit = entityManager.createNativeQuery("INSERT INTO deposit (deposit_amount, user_id, create_date) VALUES(:deposit_amount, :user_id, :create_date) " ) ;

        insertIntoDeposit.setParameter("deposit_amount", transferAmount);
        insertIntoDeposit.setParameter("user_id", user.id());
        insertIntoDeposit.setParameter("create_date", LocalDateTime.now());

        insertIntoDeposit.executeUpdate();

        return user;
    }

}
