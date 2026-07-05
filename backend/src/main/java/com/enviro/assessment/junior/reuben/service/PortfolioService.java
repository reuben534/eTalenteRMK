package com.enviro.assessment.junior.reuben.service;

import com.enviro.assessment.junior.reuben.domain.Investor;
import com.enviro.assessment.junior.reuben.dto.InvestorPortfolioDto;
import com.enviro.assessment.junior.reuben.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.reuben.repository.InvestorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PortfolioService {

    private final InvestorRepository investorRepository;

    public PortfolioService(InvestorRepository investorRepository) {
        this.investorRepository = investorRepository;
    }

    @Transactional(readOnly = true)
    public InvestorPortfolioDto getPortfolio(Long investorId) {
        Investor investor = investorRepository.findWithProductsById(investorId)
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found"));
        return PortfolioMapper.toPortfolioDto(investor);
    }
}
