package com.fintech.controller;

import com.fintech.entity.Deposit;
import com.fintech.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransferClientController {

    private final DepositService depositService;

    @PutMapping("/client/{transferAmount}/{depositId}/{userId}")
    public ResponseEntity<Deposit> debitForTransfer(@PathVariable BigDecimal transferAmount,
                                              @PathVariable Integer depositId,
                                              @PathVariable String userId) {
        return ResponseEntity.ok().body(depositService.debitForTransfer(transferAmount, depositId, userId));
    }

    @PutMapping("/client/credit/{receiverAccountNumber}/{transferAmount}")
    public void creditTheReceiver(@PathVariable Long receiverAccountNumber,
                                  @PathVariable BigDecimal transferAmount) {

        depositService.creditTheReceiver(receiverAccountNumber, transferAmount);
    }

}
