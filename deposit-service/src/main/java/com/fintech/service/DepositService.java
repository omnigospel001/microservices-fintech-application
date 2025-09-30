package com.fintech.service;

import com.fintech.mapper.Mapper;
import com.fintech.notification.DepositNotificationRequest;
import com.fintech.notification.NotificationProducer;
import com.fintech.repository.DepositRepo;
import com.fintech.entity.Deposit;
import com.fintech.request.DepositRequest;
import com.fintech.user.UserClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class DepositService {

    private final DepositRepo depositRepo;
    private final UserClient userClient;
    private final Mapper mapper;
    private final NotificationProducer notificationProducer;
    private final EntityManager entityManager;

    public Deposit deposit(DepositRequest depositRequest) {
      var user = userClient.findByUserId(depositRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

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

       Query query = entityManager.createNativeQuery("UPDATE deposit SET deposit_amount = deposit_amount - "+ withdrawalAmount +" WHERE id =:depositId AND user_id =:userId");

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

        Query query = entityManager.createNativeQuery("UPDATE deposit SET deposit_amount = deposit_amount - "+ transferAmount +" WHERE id =:depositId AND user_id =:userId");

        query.setParameter("depositId", deposit.getId());
        query.setParameter("userId", user.id());

        query.executeUpdate();

        isDepositAmountLow(userId);
        return deposit;

    }

    public void isDepositAmountLow(String userId) {

        var user = userClient.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        //Check the sender current amount is less than the transfer amount
        Query query  =  entityManager.createNativeQuery("SELECT SUM(deposit_amount) FROM deposit WHERE user_id =:user_id" );
        query.setParameter("user_id", user.id());

        double totalAmount = ((BigDecimal) query.getSingleResult()).doubleValue();

        double LIMIT_DEPOSIT_AMOUNT = 50.00;
        if (totalAmount <= LIMIT_DEPOSIT_AMOUNT){
            throw new RuntimeException("Insufficient balance");
        }

    }

    public void creditTheReceiver(Long receiverAccountNumber, BigDecimal transferAmount) {

        var user = userClient.findByAccountNumber(receiverAccountNumber)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Query insertIntoDeposit = entityManager.createNativeQuery("INSERT INTO deposit (deposit_amount, user_id, create_date) VALUES(:deposit_amount, :user_id, :create_date) " ) ;

        insertIntoDeposit.setParameter("deposit_amount", transferAmount);
        insertIntoDeposit.setParameter("user_id", user.id());
        insertIntoDeposit.setParameter("create_date", LocalDateTime.now());

        insertIntoDeposit.executeUpdate();
    }

}
