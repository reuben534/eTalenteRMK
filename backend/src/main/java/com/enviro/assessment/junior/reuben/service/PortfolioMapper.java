package com.enviro.assessment.junior.reuben.service;

import com.enviro.assessment.junior.reuben.domain.Investor;
import com.enviro.assessment.junior.reuben.domain.Product;
import com.enviro.assessment.junior.reuben.domain.WithdrawalNotice;
import com.enviro.assessment.junior.reuben.dto.InvestorPortfolioDto;
import com.enviro.assessment.junior.reuben.dto.ProductDto;
import com.enviro.assessment.junior.reuben.dto.WithdrawalNoticeDto;
import java.time.LocalDate;
import java.time.Period;

public final class PortfolioMapper {

    private PortfolioMapper() {
    }

    public static InvestorPortfolioDto toPortfolioDto(Investor investor) {
        return new InvestorPortfolioDto(
                investor.getId(),
                investor.getFirstName(),
                investor.getLastName(),
                investor.getEmail(),
                investor.getDateOfBirth(),
                age(investor.getDateOfBirth()),
                investor.getProducts().stream().map(PortfolioMapper::toProductDto).toList()
        );
    }

    public static ProductDto toProductDto(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getType(), product.getCurrentBalance());
    }

    public static WithdrawalNoticeDto toWithdrawalDto(WithdrawalNotice notice) {
        Investor investor = notice.getInvestor();
        Product product = notice.getProduct();
        return new WithdrawalNoticeDto(
                notice.getId(),
                investor.getId(),
                investor.getFirstName() + " " + investor.getLastName(),
                product.getId(),
                product.getName(),
                product.getType(),
                notice.getAmount(),
                notice.getBalanceBefore(),
                notice.getBalanceAfter(),
                notice.getCreatedAt(),
                notice.getStatus()
        );
    }

    public static int age(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}
