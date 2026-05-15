package com.bank.api.dto;

import com.bank.api.entity.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovementDTO {

    private LocalDateTime date;

    private TransactionType transactionType;

    private BigDecimal balance;

    private BigDecimal amount;
}
