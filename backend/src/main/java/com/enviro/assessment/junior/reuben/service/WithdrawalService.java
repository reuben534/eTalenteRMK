package com.enviro.assessment.junior.reuben.service;

import com.enviro.assessment.junior.reuben.domain.Investor;
import com.enviro.assessment.junior.reuben.domain.Product;
import com.enviro.assessment.junior.reuben.domain.ProductType;
import com.enviro.assessment.junior.reuben.domain.WithdrawalNotice;
import com.enviro.assessment.junior.reuben.domain.WithdrawalStatus;
import com.enviro.assessment.junior.reuben.dto.CreateWithdrawalRequest;
import com.enviro.assessment.junior.reuben.dto.WithdrawalNoticeDto;
import com.enviro.assessment.junior.reuben.exception.BusinessRuleException;
import com.enviro.assessment.junior.reuben.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.reuben.repository.InvestorRepository;
import com.enviro.assessment.junior.reuben.repository.ProductRepository;
import com.enviro.assessment.junior.reuben.repository.WithdrawalNoticeRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WithdrawalService {

    private static final BigDecimal NINETY_PERCENT = new BigDecimal("0.90");

    private final InvestorRepository investorRepository;
    private final ProductRepository productRepository;
    private final WithdrawalNoticeRepository withdrawalNoticeRepository;

    public WithdrawalService(InvestorRepository investorRepository,
                             ProductRepository productRepository,
                             WithdrawalNoticeRepository withdrawalNoticeRepository) {
        this.investorRepository = investorRepository;
        this.productRepository = productRepository;
        this.withdrawalNoticeRepository = withdrawalNoticeRepository;
    }

    @Transactional
    public WithdrawalNoticeDto create(CreateWithdrawalRequest request) {
        Investor investor = investorRepository.findById(request.investorId())
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found"));
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getInvestor().getId().equals(investor.getId())) {
            throw new BusinessRuleException("Product does not belong to this investor");
        }

        validateWithdrawal(investor, product, request.amount());

        BigDecimal balanceBefore = product.getCurrentBalance();
        product.reduceBalance(request.amount());
        WithdrawalNotice notice = new WithdrawalNotice(
                investor,
                product,
                request.amount(),
                balanceBefore,
                product.getCurrentBalance(),
                WithdrawalStatus.CREATED
        );
        return PortfolioMapper.toWithdrawalDto(withdrawalNoticeRepository.save(notice));
    }

    @Transactional(readOnly = true)
    public List<WithdrawalNoticeDto> history(Long investorId) {
        return withdrawalNoticeRepository.findByInvestorIdOrderByCreatedAtDesc(investorId)
                .stream()
                .map(PortfolioMapper::toWithdrawalDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<WithdrawalNotice> statementRows(Long investorId, LocalDate from, LocalDate to) {
        LocalDateTime start = from == null ? LocalDate.of(1900, 1, 1).atStartOfDay() : from.atStartOfDay();
        LocalDateTime end = to == null ? LocalDate.of(2999, 12, 31).atTime(LocalTime.MAX) : to.atTime(LocalTime.MAX);
        return withdrawalNoticeRepository.findByInvestorIdAndCreatedAtBetweenOrderByCreatedAtDesc(investorId, start, end);
    }

    private void validateWithdrawal(Investor investor, Product product, BigDecimal amount) {
        BigDecimal balance = product.getCurrentBalance();
        BigDecimal maxAllowed = balance.multiply(NINETY_PERCENT).setScale(2, RoundingMode.HALF_UP);

        if (product.getType() == ProductType.RETIREMENT && PortfolioMapper.age(investor.getDateOfBirth()) <= 65) {
            throw new BusinessRuleException("Retirement withdrawals are only allowed when the investor is older than 65");
        }
        if (amount.compareTo(balance) > 0) {
            throw new BusinessRuleException("Withdrawal cannot exceed the current balance");
        }
        if (amount.compareTo(maxAllowed) > 0) {
            throw new BusinessRuleException("Withdrawal cannot exceed 90% of the current balance");
        }
    }
}
