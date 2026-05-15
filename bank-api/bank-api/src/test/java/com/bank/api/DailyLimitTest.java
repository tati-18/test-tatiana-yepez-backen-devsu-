package com.bank.api;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.bank.api.dto.TransactionDTO;
import com.bank.api.entity.Account;
import com.bank.api.entity.TransactionType;
import com.bank.api.repository.AccountRepository;
import com.bank.api.service.TransactionService;

@Transactional
@Rollback
public class DailyLimitTest extends BaseIntegrationTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldThrowExceptionWhenDailyLimitIsExceeded() {

        Account account = new Account();
        account.setAccountNumber(
                "ACC-DAILY-" + System.currentTimeMillis());
        account.setAccountType("SAVINGS");
        account.setInitialBalance(new BigDecimal("5000"));
        account.setCurrentBalance(new BigDecimal("5000"));
        account.setStatus(true);

        Account savedAccount = accountRepository.save(account);

        TransactionDTO firstTransaction = new TransactionDTO();

        firstTransaction.setAccountId(savedAccount.getId());
        firstTransaction.setTransactionType(TransactionType.DEBIT);
        firstTransaction.setAmount(new BigDecimal("800"));

        transactionService.create(firstTransaction);

        TransactionDTO secondTransaction = new TransactionDTO();

        secondTransaction.setAccountId(savedAccount.getId());
        secondTransaction.setTransactionType(TransactionType.DEBIT);
        secondTransaction.setAmount(new BigDecimal("300"));

        assertThrows(RuntimeException.class, () -> {

            transactionService.create(secondTransaction);

        });
    }
}