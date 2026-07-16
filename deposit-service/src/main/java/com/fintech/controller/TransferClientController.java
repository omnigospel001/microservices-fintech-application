package com.fintech.controller;

import com.fintech.entity.Deposit;
import com.fintech.service.DepositService;
import com.fintech.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/client/credit/{accountNumber}/{transferAmount}")
    public ResponseEntity<UserResponse> creditTheReceiver(@PathVariable Long accountNumber,
                                                          @PathVariable BigDecimal transferAmount) {

       return ResponseEntity.ok().body(depositService.creditTheReceiver(accountNumber, transferAmount));
    }

}
