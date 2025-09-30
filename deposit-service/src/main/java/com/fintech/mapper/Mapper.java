package com.fintech.mapper;

import com.fintech.entity.Deposit;
import com.fintech.request.DepositRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class Mapper {

    public Deposit mapDeposit(DepositRequest depositRequest) {
        return  Deposit.builder()
                .depositAmount(depositRequest.getDepositAmount())
                .userId(depositRequest.getUserId())
                .createdDate(LocalDateTime.now())
                .build();
    }
}
