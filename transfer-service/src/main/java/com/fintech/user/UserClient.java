package com.fintech.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/user/{id}")
    Optional<SenderResponse> findByUserId(@PathVariable("id") String userId);

    @GetMapping("/user/account/{accountNumber}")
    Optional<ReceiverResponse> findByAccountNumber(@PathVariable("accountNumber") Long accountNumber);

}
