package com.fintech.repository;

import com.fintech.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepo extends JpaRepository<Deposit, Integer> {
}
