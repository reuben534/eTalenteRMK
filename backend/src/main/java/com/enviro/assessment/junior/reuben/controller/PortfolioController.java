package com.enviro.assessment.junior.reuben.controller;

import com.enviro.assessment.junior.reuben.dto.InvestorPortfolioDto;
import com.enviro.assessment.junior.reuben.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/investors")
@Tag(name = "Portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/{investorId}/portfolio")
    @Operation(summary = "Retrieve investor details and products")
    public InvestorPortfolioDto getPortfolio(@PathVariable Long investorId) {
        return portfolioService.getPortfolio(investorId);
    }
}
