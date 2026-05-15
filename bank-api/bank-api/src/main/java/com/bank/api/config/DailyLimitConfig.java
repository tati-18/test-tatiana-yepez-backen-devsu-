package com.bank.api.config;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DailyLimitConfig {

    private static final BigDecimal DAILY_LIMIT =
            new BigDecimal("1000");

    public BigDecimal getDailyLimit() {
        return DAILY_LIMIT;
    }
}