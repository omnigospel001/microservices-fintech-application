package com.fintech.controller;

import com.fintech.request.TransferRequest;
import com.fintech.response.TransferResponse;
import com.fintech.service.TransferService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransferController {

   private final TransferService transferService;

    @PutMapping
    public ResponseEntity<TransferResponse> transfer(@RequestBody @Valid TransferRequest transferRequest) {
        return ResponseEntity.ok().body(transferService.transfer(transferRequest));
    }

}
