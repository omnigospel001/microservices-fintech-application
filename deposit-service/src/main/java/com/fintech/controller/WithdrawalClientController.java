package com.fintech.controller;

import com.fintech.entity.Deposit;
import com.fintech.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/withdrawal")
@RequiredArgsConstructor
public class WithdrawalClientController {

    private final DepositService depositService;

    @PutMapping("/client/{withdrawalAmount}/{depositId}/{userId}")
    public ResponseEntity<Deposit> withdrawal(@PathVariable BigDecimal withdrawalAmount,
                                              @PathVariable Integer depositId,
                                              @PathVariable String userId) {
        return ResponseEntity.ok().body(depositService.withdrawal(withdrawalAmount, depositId, userId));
    }

}
