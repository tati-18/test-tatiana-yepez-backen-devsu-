package com.bank.api.service.impl;

import com.bank.api.dto.TransactionDTO;
import com.bank.api.entity.Account;
import com.bank.api.entity.Transaction;
import com.bank.api.repository.AccountRepository;
import com.bank.api.repository.TransactionRepository;
import com.bank.api.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.bank.api.entity.TransactionType;
import com.bank.api.strategy.CreditStrategy;
import com.bank.api.strategy.DebitStrategy;
import com.bank.api.config.DailyLimitConfig;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CreditStrategy creditStrategy;
    private final DebitStrategy debitStrategy;
    private final DailyLimitConfig dailyLimitConfig;

    @Override
    @Transactional
    public Transaction create(TransactionDTO dto) {

        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BigDecimal currentBalance = account.getCurrentBalance();

        BigDecimal newBalance;

        if (dto.getTransactionType() == TransactionType.CREDIT) {

            newBalance = creditStrategy.execute(
                    currentBalance,
                    dto.getAmount());

        } else {

            LocalDateTime start = LocalDate.now().atStartOfDay();

            LocalDateTime end = LocalDate.now().atTime(23, 59, 59);

            BigDecimal dailyDebit = transactionRepository.getDailyDebitTotal(
                    account.getId(),
                    start,
                    end);

            BigDecimal totalDebit = dailyDebit.abs()
                    .add(dto.getAmount().abs());

            if (totalDebit.compareTo(
                    dailyLimitConfig.getDailyLimit()) > 0) {

                throw new RuntimeException(
                        "Daily limit exceeded");
            }

            newBalance = debitStrategy.execute(
                    currentBalance,
                    dto.getAmount());

            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {

                throw new RuntimeException(
                        "Balance not available");
            }
        }

        account.setCurrentBalance(newBalance);

        accountRepository.save(account);

        Transaction transaction = new Transaction();

        transaction.setDate(LocalDateTime.now());

        transaction.setTransactionType(
                dto.getTransactionType());

        BigDecimal amount = dto.getTransactionType() == TransactionType.DEBIT
                ? dto.getAmount().abs().negate()
                : dto.getAmount().abs();

        transaction.setAmount(amount);

        transaction.setBalance(newBalance);

        transaction.setAccount(account);

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAll() {

        return transactionRepository.findAll();
    }
}