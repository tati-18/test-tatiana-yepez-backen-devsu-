package com.bank.api.repository;

import com.bank.api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
                SELECT COALESCE(SUM(t.amount), 0)
                FROM Transaction t
                WHERE t.account.id = :accountId
                AND t.date BETWEEN :startDate AND :endDate
                AND t.amount < 0
            """)
    BigDecimal getDailyDebitTotal(
            Long accountId,
            LocalDateTime startDate,
            LocalDateTime endDate);

    List<Transaction> findByAccountIdAndDateBetweenOrderByDateAsc(
            Long accountId,
            LocalDateTime startDate,
            LocalDateTime endDate);

    @Query("""
                SELECT COALESCE(SUM(t.amount), 0)
                FROM Transaction t
                WHERE t.account.id = :accountId
                AND t.date BETWEEN :startDate AND :endDate
                AND t.amount > 0
            """)
    BigDecimal getCreditTotal(
            Long accountId,
            LocalDateTime startDate,
            LocalDateTime endDate);
}