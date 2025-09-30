package com.fintech.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DepositRequest {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive number")
    private BigDecimal depositAmount;

    @NotBlank(message = "user Id is required")
    private String userId;

}
