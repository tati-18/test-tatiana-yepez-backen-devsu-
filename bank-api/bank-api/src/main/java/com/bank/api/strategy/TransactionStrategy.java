package com.bank.api.strategy;

import java.math.BigDecimal;

public interface TransactionStrategy {

    BigDecimal execute(BigDecimal currentBalance,
                       BigDecimal value);
}