package com.fintech.request;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class TransferRequest {

    private Long receiverAccountNumber;
    private BigDecimal transferAmount;
    private Integer depositId;
    private String userId;

}
