package com.fintech.deposit;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.math.BigDecimal;
import java.util.Optional;

@FeignClient(name = "deposit-service")
public interface DepositClient {

    @PutMapping("/transfer/client/{transferAmount}/{depositId}/{userId}")
    Optional<DepositResponse> debitForTransfer(@PathVariable BigDecimal transferAmount,
                                         @PathVariable Integer depositId,
                                         @PathVariable String userId);


    @PutMapping("/transfer/client/credit/{accountNumber}/{transferAmount}")
    Optional<DepositResponse> creditTheReceiver(@PathVariable Long accountNumber,
                                               @PathVariable BigDecimal transferAmount);

}