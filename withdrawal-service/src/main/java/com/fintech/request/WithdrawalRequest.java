package com.fintech.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class WithdrawalRequest {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive number")
    private BigDecimal withdrawalAmount;

    @NotBlank(message = "deposit Id is required")
    private Integer depositId;

    @NotBlank(message = "user Id is required")
    private String userId;

}
