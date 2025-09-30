package com.fintech.controller;

import com.fintech.entity.Deposit;
import com.fintech.request.DepositRequest;
import com.fintech.service.DepositService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deposit")
@RequiredArgsConstructor
public class DepositController {

    private final DepositService depositService;

    @PostMapping
    public ResponseEntity<Deposit> saveUser(@RequestBody @Valid DepositRequest depositRequest) {
        return ResponseEntity.ok().body(depositService.deposit(depositRequest));
    }



}
