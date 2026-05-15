package com.bank.api.service;

import java.time.LocalDate;

import com.bank.api.dto.ReporteDTO;

public interface ReportService {

    ReporteDTO getDataReport(Long clienteId, LocalDate startDate, LocalDate endDate);

    String downloadPdfReport(Long clienteId, LocalDate startDate, LocalDate endDate);
}
