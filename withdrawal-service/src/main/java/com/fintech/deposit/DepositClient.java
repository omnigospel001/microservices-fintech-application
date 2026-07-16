package com.fintech.deposit;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.math.BigDecimal;
import java.util.Optional;

@FeignClient(name = "deposit-service")
public interface DepositClient {
    @PutMapping("/withdrawal/client/{withdrawalAmount}/{depositId}/{userId}")
    Optional<DepositResponse> withdrawal(@PathVariable BigDecimal withdrawalAmount,
                                         @PathVariable Integer depositId,
                                         @PathVariable String userId);

}