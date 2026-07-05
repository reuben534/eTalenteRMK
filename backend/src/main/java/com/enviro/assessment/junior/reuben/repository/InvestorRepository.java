package com.enviro.assessment.junior.reuben.repository;

import com.enviro.assessment.junior.reuben.domain.Investor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InvestorRepository extends JpaRepository<Investor, Long> {

    @EntityGraph(attributePaths = "products")
    Optional<Investor> findWithProductsById(Long id);
}
