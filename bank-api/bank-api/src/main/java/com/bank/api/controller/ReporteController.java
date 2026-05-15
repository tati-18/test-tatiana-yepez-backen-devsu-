package com.bank.api.controller;

import java.time.LocalDate;

import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.api.dto.ApiResponse;
import com.bank.api.dto.ReporteDTO;
import com.bank.api.service.ReportService;
import com.bank.api.util.LocaleUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReporteController {

    private final ReportService reporteService;
    private final MessageSource messageSource;
    private final LocaleUtil localeUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<ReporteDTO>> obtenerReporte(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        ReporteDTO reporte = reporteService.getDataReport(clienteId, startDate, endDate);
        String message = messageSource.getMessage("messages.report.generated", null, localeUtil.getLocale());
        return ResponseEntity.ok(ApiResponse.<ReporteDTO>builder()
                .data(reporte)
                .code(200)
                .message(message)
                .build());
    }

    @GetMapping("/download-pdf")
    public ResponseEntity<ApiResponse<String>>downloadPdf(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String pdfBase64 = reporteService.downloadPdfReport(clienteId, startDate, endDate);
        String message = messageSource.getMessage("messages.report.generated", null, localeUtil.getLocale());
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .data(pdfBase64)
                .code(200)
                .message(message)
                .build());
    }
}
