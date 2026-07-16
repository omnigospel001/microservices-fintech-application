package com.fintech.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class TransferRequest {

    @NotNull(message = "Receiver account number is required")
    @Positive(message = "Account number must be positive")
    private Long accountNumber;

    @NotNull(message = "Transfer amount is required")
    @Positive(message = "Transfer amount must be positive")
    private BigDecimal transferAmount;

    @NotNull(message = "Deposit ID is required")
    @Positive(message = "Deposit ID must be positive")
    private Integer depositId;

    @NotBlank(message = "User ID is required")
    private String userId;

}
