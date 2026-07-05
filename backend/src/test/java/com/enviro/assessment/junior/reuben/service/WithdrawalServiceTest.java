package com.enviro.assessment.junior.reuben.service;

import com.enviro.assessment.junior.reuben.domain.Investor;
import com.enviro.assessment.junior.reuben.domain.Product;
import com.enviro.assessment.junior.reuben.domain.ProductType;
import com.enviro.assessment.junior.reuben.dto.CreateWithdrawalRequest;
import com.enviro.assessment.junior.reuben.exception.BusinessRuleException;
import com.enviro.assessment.junior.reuben.repository.InvestorRepository;
import com.enviro.assessment.junior.reuben.repository.ProductRepository;
import com.enviro.assessment.junior.reuben.repository.WithdrawalNoticeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class WithdrawalServiceTest {

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private InvestorRepository investorRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WithdrawalNoticeRepository withdrawalNoticeRepository;

    private Investor olderInvestor;
    private Investor youngerInvestor;
    private Product retirementProduct;
    private Product savingsProduct;

    @BeforeEach
    void setUp() {
        withdrawalNoticeRepository.deleteAll();
        productRepository.deleteAll();
        investorRepository.deleteAll();

        olderInvestor = new Investor("Asha", "Pillay", LocalDate.now().minusYears(70), "asha@example.com");
        retirementProduct = new Product("Retirement", ProductType.RETIREMENT, new BigDecimal("1000.00"));
        olderInvestor.addProduct(retirementProduct);
        investorRepository.save(olderInvestor);

        youngerInvestor = new Investor("Ben", "Jacobs", LocalDate.now().minusYears(40), "ben@example.com");
        savingsProduct = new Product("Savings", ProductType.SAVINGS, new BigDecimal("500.00"));
        youngerInvestor.addProduct(savingsProduct);
        investorRepository.save(youngerInvestor);
    }

    @Test
    void createsWithdrawalAndUpdatesBalance() {
        var response = withdrawalService.create(new CreateWithdrawalRequest(
                olderInvestor.getId(),
                retirementProduct.getId(),
                new BigDecimal("250.00")
        ));

        assertThat(response.balanceBefore()).isEqualByComparingTo("1000.00");
        assertThat(response.balanceAfter()).isEqualByComparingTo("750.00");
    }

    @Test
    void rejectsRetirementWithdrawalForInvestorNotOlderThan65() {
        Product youngRetirement = new Product("Young Retirement", ProductType.RETIREMENT, new BigDecimal("1000.00"));
        youngerInvestor.addProduct(youngRetirement);
        youngerInvestor = investorRepository.save(youngerInvestor);
        
        Long productId = youngerInvestor.getProducts().stream()
                .filter(p -> p.getType() == ProductType.RETIREMENT)
                .map(Product::getId)
                .findFirst()
                .orElseThrow();

        assertThatThrownBy(() -> withdrawalService.create(new CreateWithdrawalRequest(
                youngerInvestor.getId(),
                productId,
                new BigDecimal("100.00")
        ))).isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("older than 65");
    }

    @Test
    void rejectsWithdrawalAboveNinetyPercent() {
        assertThatThrownBy(() -> withdrawalService.create(new CreateWithdrawalRequest(
                youngerInvestor.getId(),
                savingsProduct.getId(),
                new BigDecimal("451.00")
        ))).isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("90%");
    }
}
