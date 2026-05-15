package com.bank.api.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CreditStrategy implements TransactionStrategy {

    @Override
    public BigDecimal execute(BigDecimal currentBalance,
                              BigDecimal value) {

        return currentBalance.add(value);
    }
}