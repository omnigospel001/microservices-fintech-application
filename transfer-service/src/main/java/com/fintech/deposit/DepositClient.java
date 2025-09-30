package com.fintech.deposit;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.Optional;

@FeignClient(name = "deposit-service")
public interface DepositClient {

    @GetMapping("/transfer/client/{transferAmount}/{depositId}/{userId}")
    Optional<DepositResponse> debitForTransfer(@PathVariable BigDecimal transferAmount,
                                         @PathVariable Integer depositId,
                                         @PathVariable String userId);


    @GetMapping("/transfer/client/credit/{receiverAccountNumber}/{transferAmount}")
    Optional<DepositResponse> creditTheReceiver(@PathVariable Long receiverAccountNumber,
                                               @PathVariable BigDecimal transferAmount);

}