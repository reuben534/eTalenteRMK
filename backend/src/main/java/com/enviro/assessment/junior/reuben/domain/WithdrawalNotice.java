package com.enviro.assessment.junior.reuben.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class WithdrawalNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Investor investor;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private WithdrawalStatus status;

    protected WithdrawalNotice() {
    }

    public WithdrawalNotice(Investor investor, Product product, BigDecimal amount,
                            BigDecimal balanceBefore, BigDecimal balanceAfter,
                            WithdrawalStatus status) {
        this.investor = investor;
        this.product = product;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Investor getInvestor() {
        return investor;
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public WithdrawalStatus getStatus() {
        return status;
    }
}
