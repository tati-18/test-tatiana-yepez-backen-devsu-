package com.bank.api;

import java.math.BigDecimal;
import java.util.UUID;

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
public class TransactionServiceTest extends BaseIntegrationTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldThrowExceptionWhenBalanceIsInsufficient() {

        Account account = new Account();

        account.setAccountNumber("ACC-" + UUID.randomUUID());
        account.setAccountType("SAVINGS");
        account.setInitialBalance(new BigDecimal("100"));
        account.setCurrentBalance(new BigDecimal("100"));
        account.setStatus(true);

        Account savedAccount =
                accountRepository.save(account);

        TransactionDTO dto = new TransactionDTO();

        dto.setAccountId(savedAccount.getId());
        dto.setTransactionType(TransactionType.DEBIT);
        dto.setAmount(new BigDecimal("500"));

        assertThrows(RuntimeException.class, () -> {
            transactionService.create(dto);
        });
    }
}