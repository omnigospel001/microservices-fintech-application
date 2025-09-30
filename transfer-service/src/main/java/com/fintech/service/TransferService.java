package com.fintech.service;

import com.fintech.deposit.DepositClient;
import com.fintech.notification.NotificationProducer;
import com.fintech.notification.TransferNotificationRequest;
import com.fintech.request.TransferRequest;
import com.fintech.response.TransferResponse;
import com.fintech.user.UserClient;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TransferService {

    private final UserClient userClient;
    private final DepositClient depositClient;
    private final NotificationProducer notificationProducer;


    public TransferResponse transfer(TransferRequest transferRequest) {

        var sender = userClient.findByUserId(transferRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        var receiver = userClient.findByAccountNumber(transferRequest.getReceiverAccountNumber())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (sender.accountNumber().equals(receiver.accountNumber())) {
            throw new RuntimeException("You cannot transfer money to yourself");
        }

        //
        var debitResponse = depositClient.debitForTransfer(transferRequest.getTransferAmount(), transferRequest.getDepositId(), transferRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        var creditResponse = depositClient.creditTheReceiver(receiver.accountNumber(), transferRequest.getTransferAmount())
                .orElseThrow(() -> new NotFoundException("User not found"));

        notificationProducer.sendNotification(
                new TransferNotificationRequest(
                        sender.firstName(),
                        sender.lastName(),
                        sender.email(),
                        sender.accountNumber(),
                        receiver.firstName(),
                        receiver.lastName(),
                        receiver.email(),
                        receiver.accountNumber(),
                        transferRequest.getTransferAmount()
                )
        );


        return new TransferResponse(
                receiver.accountNumber(),
                transferRequest.getTransferAmount(),
                debitResponse.id(),
                creditResponse.userId()
        );


    }
}
