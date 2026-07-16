package com.fintech.repository;

import com.fintech.entity.Settlement;
import com.fintech.enums.SettlementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SettlementRepo extends JpaRepository<Settlement, Integer> {

    List<Settlement> findByUserId(String userId);

    List<Settlement> findByStatus(SettlementStatus status);

    Optional<Settlement> findByReferenceId(String referenceId);

}
