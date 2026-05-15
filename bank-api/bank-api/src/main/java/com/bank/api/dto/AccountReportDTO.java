package com.bank.api.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountReportDTO {

    private String accountNumber;

    private String accountType;

    private BigDecimal initialBalance;

    private BigDecimal currentBalance;

    private BigDecimal creditTotal;

    private BigDecimal debitTotal;

    private List<MovementDTO> movements;
}
