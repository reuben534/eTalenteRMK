package com.enviro.assessment.junior.reuben.config;

import com.enviro.assessment.junior.reuben.domain.Investor;
import com.enviro.assessment.junior.reuben.domain.Product;
import com.enviro.assessment.junior.reuben.domain.ProductType;
import com.enviro.assessment.junior.reuben.repository.InvestorRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(InvestorRepository investorRepository) {
        return args -> {
            Investor grace = new Investor("Grace", "Mokoena", LocalDate.of(1954, 4, 12), "grace@example.com");
            grace.addProduct(new Product("Retirement Annuity", ProductType.RETIREMENT, new BigDecimal("500000.00")));
            grace.addProduct(new Product("Tax Free Savings", ProductType.TAX_FREE, new BigDecimal("85000.00")));

            Investor thabo = new Investor("Thabo", "Dlamini", LocalDate.of(1988, 9, 3), "thabo@example.com");
            thabo.addProduct(new Product("Retirement Preservation", ProductType.RETIREMENT, new BigDecimal("275000.00")));
            thabo.addProduct(new Product("Money Market", ProductType.SAVINGS, new BigDecimal("42000.00")));

            investorRepository.save(grace);
            investorRepository.save(thabo);
        };
    }
}
