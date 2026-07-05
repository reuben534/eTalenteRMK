package com.enviro.assessment.junior.reuben.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateWithdrawalRequest(
        @NotNull(message = "Investor id is required")
        Long investorId,

        @NotNull(message = "Product id is required")
        Long productId,

        @NotNull(message = "Withdrawal amount is required")
        @DecimalMin(value = "0.01", message = "Withdrawal amount must be greater than zero")
        BigDecimal amount
) {
}
