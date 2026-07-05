package com.enviro.assessment.junior.reuben.dto;

import com.enviro.assessment.junior.reuben.domain.ProductType;
import com.enviro.assessment.junior.reuben.domain.WithdrawalStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WithdrawalNoticeDto(
        Long id,
        Long investorId,
        String investorName,
        Long productId,
        String productName,
        ProductType productType,
        BigDecimal amount,
        BigDecimal balanceBefore,
        BigDecimal balanceAfter,
        LocalDateTime createdAt,
        WithdrawalStatus status
) {
}
