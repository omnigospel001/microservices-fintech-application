package com.fintech.service;

import com.fintech.deposit.DepositClient;
import com.fintech.entity.Transactions;
import com.fintech.notification.NotificationProducer;
import com.fintech.notification.TransferNotificationRequest;
import com.fintech.repository.TransactionsRepo;
import com.fintech.request.TransferRequest;
import com.fintech.response.TransferResponse;
import com.fintech.user.UserClient;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final UserClient userClient;
    private final DepositClient depositClient;
    private final NotificationProducer notificationProducer;
    private final TransactionsRepo transactionsRepo;


    public TransferResponse transfer(TransferRequest transferRequest) {

        var sender = userClient.findByUserId(transferRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        var receiver = userClient.findByAccountNumber(transferRequest.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (sender.accountNumber().equals(receiver.accountNumber())) {
            throw new RuntimeException("You cannot transfer money to yourself");
        }

        //
        var debitResponse = depositClient.debitForTransfer(transferRequest.getTransferAmount(), transferRequest.getDepositId(), transferRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        log.info("debitResponse: {}", debitResponse);

        log.info("receiver.accountNumber(): {}", receiver.accountNumber());


        var creditResponse = depositClient.creditTheReceiver(receiver.accountNumber(), transferRequest.getTransferAmount())
                .orElseThrow(() -> new NotFoundException("User not found"));

        //
        Transactions transactions = new Transactions();
        transactions.setSenderFirstName(sender.firstName());
        transactions.setSenderLastName(sender.lastName());
        transactions.setSenderEmail(sender.email());
        transactions.setSenderAccountNumber(sender.accountNumber());

        transactions.setReceiverFirstName(receiver.firstName());
        transactions.setReceiverLastName(receiver.lastName());
        transactions.setReceiverEmail(receiver.email());
        transactions.setReceiverAccountNumber(receiver.accountNumber());
        transactions.setTransferAmount(transferRequest.getTransferAmount());
        transactions.setTransferDate(LocalDateTime.now());

        transactionsRepo.save(transactions);


        //
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
                creditResponse.userId()
        );


    }
}
