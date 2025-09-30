package com.fintech.controller;

import com.fintech.request.WithdrawalRequest;
import com.fintech.response.WithdrawalResponse;
import com.fintech.service.WithdrawalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/withdraw")
@RequiredArgsConstructor
public class WithdrawalController {

   private final WithdrawalService withdrawalService;

    @PutMapping
    public ResponseEntity<WithdrawalResponse> withdrawal(@RequestBody @Valid WithdrawalRequest withdrawalRequest) {
        return ResponseEntity.ok().body(withdrawalService.withdraw(withdrawalRequest));
    }

}
