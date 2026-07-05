package com.enviro.assessment.junior.reuben.dto;

import java.time.LocalDate;
import java.util.List;

public record InvestorPortfolioDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        LocalDate dateOfBirth,
        int age,
        List<ProductDto> products
) {
}
