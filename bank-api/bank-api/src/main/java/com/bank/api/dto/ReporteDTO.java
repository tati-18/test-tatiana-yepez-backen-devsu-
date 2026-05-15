package com.bank.api.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteDTO {

    private Long clienteId;

    private String name;

    private String identificacion;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<AccountReportDTO> accounts;

    private String pdfBase64;
}
