package com.fintech.service;

import com.fintech.deposit.DepositClient;
import com.fintech.notification.NotificationProducer;
import com.fintech.notification.WithdrawalNotificationRequest;
import com.fintech.request.WithdrawalRequest;
import com.fintech.response.WithdrawalResponse;
import com.fintech.user.UserClient;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WithdrawalService {

    private final UserClient userClient;
    private final DepositClient depositClient;
    private final NotificationProducer notificationProducer;


    public WithdrawalResponse withdraw(WithdrawalRequest withdrawalRequest) {

        var deposit = depositClient.withdrawal(withdrawalRequest.getWithdrawalAmount(), withdrawalRequest.getDepositId(), withdrawalRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        var user = userClient.findByUserId(withdrawalRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        notificationProducer.sendNotification(
                new WithdrawalNotificationRequest(
                        user.firstName(),
                        user.lastName(),
                        user.email(),
                        user.accountNumber(),
                        withdrawalRequest.getWithdrawalAmount()
                )
        );

        return new WithdrawalResponse(
                withdrawalRequest.getWithdrawalAmount(),
                deposit.userId()
        );
    }
}
