package com.enviro.assessment.junior.reuben.dto;

import com.enviro.assessment.junior.reuben.domain.ProductType;
import java.math.BigDecimal;

public record ProductDto(
        Long id,
        String name,
        ProductType type,
        BigDecimal currentBalance
) {
}
