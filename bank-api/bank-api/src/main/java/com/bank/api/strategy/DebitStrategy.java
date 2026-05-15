package com.bank.api.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DebitStrategy implements TransactionStrategy {

    @Override
    public BigDecimal execute(BigDecimal currentBalance,
                              BigDecimal value) {

        return currentBalance.subtract(value.abs());
    }
}