package com.enviro.assessment.junior.reuben.repository;

import com.enviro.assessment.junior.reuben.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
