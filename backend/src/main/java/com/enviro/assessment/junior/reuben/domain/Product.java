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

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    private BigDecimal currentBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    private Investor investor;

    protected Product() {
    }

    public Product(String name, ProductType type, BigDecimal currentBalance) {
        this.name = name;
        this.type = type;
        this.currentBalance = currentBalance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProductType getType() {
        return type;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public Investor getInvestor() {
        return investor;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }

    public void reduceBalance(BigDecimal amount) {
        currentBalance = currentBalance.subtract(amount);
    }
}
