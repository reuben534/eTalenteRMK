package com.enviro.assessment.junior.reuben.repository;

import com.enviro.assessment.junior.reuben.domain.WithdrawalNotice;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalNoticeRepository extends JpaRepository<WithdrawalNotice, Long> {

    @EntityGraph(attributePaths = {"investor", "product"})
    List<WithdrawalNotice> findByInvestorIdOrderByCreatedAtDesc(Long investorId);

    @EntityGraph(attributePaths = {"investor", "product"})
    List<WithdrawalNotice> findByInvestorIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            Long investorId,
            LocalDateTime from,
            LocalDateTime to
    );
}
