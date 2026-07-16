package com.fintech.repository;

import com.fintech.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRepo extends JpaRepository<Transactions, Integer> {
}
