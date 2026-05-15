package com.bank.api.service.impl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bank.api.dto.AccountReportDTO;
import com.bank.api.dto.MovementDTO;
import com.bank.api.dto.ReporteDTO;
import com.bank.api.entity.Account;
import com.bank.api.entity.Client;
import com.bank.api.entity.Transaction;
import com.bank.api.repository.AccountRepository;
import com.bank.api.repository.ClientRepository;
import com.bank.api.repository.TransactionRepository;
import com.bank.api.service.ReportService;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

        private final ClientRepository clientRepository;
        private final AccountRepository accountRepository;
        private final TransactionRepository transactionRepository;

        private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        @Override
        public ReporteDTO getDataReport(Long clienteId,
                        LocalDate startDate,
                        LocalDate endDate) {

                Client client = clientRepository.findById(clienteId)
                                .orElseThrow(() -> new RuntimeException("Client not found"));

                LocalDateTime start = startDate.atStartOfDay();
                LocalDateTime end = endDate.atTime(23, 59, 59);

                List<Account> accounts = accountRepository.findByClientId(clienteId);

                List<AccountReportDTO> accountsReport = accounts.stream()
                                .map(account -> buildCuentaReporte(account, start, end))
                                .toList();

                return ReporteDTO.builder()
                                .clienteId(clienteId)
                                .name(client.getName())
                                .identificacion(client.getIdentification())
                                .startDate(startDate)
                                .endDate(endDate)
                                .accounts(accountsReport)
                                .build();
        }

        @Override
        public String downloadPdfReport(Long clienteId,
                        LocalDate startDate,
                        LocalDate endDate) {

                Client client = clientRepository.findById(clienteId)
                                .orElseThrow(() -> new RuntimeException("Client not found"));

                LocalDateTime start = startDate.atStartOfDay();
                LocalDateTime end = endDate.atTime(23, 59, 59);

                List<Account> accounts = accountRepository.findByClientId(clienteId);

                List<AccountReportDTO> accountsReport = accounts.stream()
                                .map(account -> buildCuentaReporte(account, start, end))
                                .toList();

                return generarPdf(client, startDate, endDate, accountsReport);
        }

        private AccountReportDTO buildCuentaReporte(Account account,
                        LocalDateTime start,
                        LocalDateTime end) {

                List<Transaction> transactions = transactionRepository.findByAccountIdAndDateBetweenOrderByDateAsc(
                                account.getId(), start, end);

                List<MovementDTO> movements = transactions.stream()
                                .map(t -> MovementDTO.builder()
                                                .date(t.getDate())
                                                .transactionType(t.getTransactionType())
                                                .amount(t.getAmount())
                                                .balance(t.getBalance())
                                                .build())
                                .toList();

                BigDecimal totalCreditos = transactionRepository
                                .getCreditTotal(account.getId(), start, end);

                BigDecimal debitTotal = transactionRepository
                                .getDailyDebitTotal(account.getId(), start, end);

                return AccountReportDTO.builder()
                                .accountNumber(account.getAccountNumber())
                                .accountType(account.getAccountType())
                                .initialBalance(account.getInitialBalance())
                                .currentBalance(account.getCurrentBalance())
                                .creditTotal(totalCreditos)
                                .debitTotal(debitTotal)
                                .movements(movements)
                                .build();
        }

        private String generarPdf(Client client,
                        LocalDate startDate,
                        LocalDate endDate,
                        List<AccountReportDTO> cuentas) {

                try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
                        PdfWriter.getInstance(document, out);
                        document.open();

                        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD, Color.DARK_GRAY);
                        Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
                        Font bodyFont = new Font(Font.HELVETICA, 9, Font.NORMAL);
                        Font labelFont = new Font(Font.HELVETICA, 9, Font.BOLD);

                        // ── Título ──────────────────────────────────────────────
                        Paragraph title = new Paragraph("Estado de Cuenta", titleFont);
                        title.setAlignment(Element.ALIGN_CENTER);
                        document.add(title);
                        document.add(Chunk.NEWLINE);

                        // ── Info cliente ─────────────────────────────────────────
                        PdfPTable infoTable = new PdfPTable(2);
                        infoTable.setWidthPercentage(100);
                        infoTable.setSpacingAfter(10f);
                        addInfoRow(infoTable, "Cliente:", client.getName(), labelFont, bodyFont);
                        addInfoRow(infoTable, "Identificación:", client.getIdentification(), labelFont, bodyFont);
                        addInfoRow(infoTable, "Período:",
                                        startDate + " al " + endDate, labelFont, bodyFont);
                        document.add(infoTable);

                        for (AccountReportDTO accountReport : cuentas) {
                                PdfPTable cuentaHeader = new PdfPTable(1);
                                cuentaHeader.setWidthPercentage(100);
                                cuentaHeader.setSpacingBefore(10f);
                                PdfPCell cuentaCell = new PdfPCell(
                                                new Phrase(accountReport.getAccountNumber() + "  |  " +
                                                                accountReport.getAccountType(), headerFont));
                                cuentaCell.setBackgroundColor(new Color(41, 98, 155));
                                cuentaCell.setPadding(6f);
                                cuentaHeader.addCell(cuentaCell);
                                document.add(cuentaHeader);

                                // Resumen de cuenta
                                PdfPTable resumen = new PdfPTable(4);
                                resumen.setWidthPercentage(100);
                                resumen.setWidths(new float[] { 1, 1, 1, 1 });
                                addResumenCell(resumen, "Saldo inicial",
                                                accountReport.getInitialBalance().toPlainString(), labelFont, bodyFont);
                                addResumenCell(resumen, "Saldo actual",
                                                accountReport.getCurrentBalance().toPlainString(), labelFont, bodyFont);
                                addResumenCell(resumen, "Total créditos",
                                                accountReport.getCreditTotal().toPlainString(), labelFont, bodyFont);
                                addResumenCell(resumen, "Total débitos",
                                                accountReport.getDebitTotal().toPlainString(), labelFont, bodyFont);
                                document.add(resumen);

                                if (!accountReport.getMovements().isEmpty()) {

                                        PdfPTable movTable = new PdfPTable(4);
                                        movTable.setWidthPercentage(100);
                                        movTable.setWidths(new float[] { 2.5f, 1.2f, 1.2f, 1.2f });
                                        movTable.setSpacingBefore(4f);

                                        for (String col : new String[] { "Fecha", "Tipo", "Valor", "Saldo" }) {
                                                PdfPCell h = new PdfPCell(new Phrase(col, headerFont));
                                                h.setBackgroundColor(new Color(100, 149, 200));
                                                h.setPadding(4f);
                                                movTable.addCell(h);
                                        }

                                        boolean alt = false;
                                        for (MovementDTO mov : accountReport.getMovements()) {
                                                Color rowColor = alt ? new Color(235, 243, 255) : Color.WHITE;
                                                addMovRow(movTable, bodyFont, rowColor,
                                                                mov.getDate().format(DATE_FMT),
                                                                mov.getTransactionType().name(),
                                                                mov.getAmount().toPlainString(),
                                                                mov.getBalance().toPlainString());
                                                alt = !alt;
                                        }
                                        document.add(movTable);

                                } else {
                                        Paragraph noMov = new Paragraph(
                                                        "Sin movimientos en el período.", bodyFont);
                                        noMov.setSpacingBefore(4f);
                                        document.add(noMov);
                                }
                        }

                        document.close();
                        return Base64.getEncoder().encodeToString(out.toByteArray());

                } catch (Exception e) {
                        throw new RuntimeException("Error generating PDF report", e);
                }
        }

        private void addInfoRow(PdfPTable table, String label, String value,
                        Font labelFont, Font bodyFont) {
                PdfPCell lCell = new PdfPCell(new Phrase(label, labelFont));
                lCell.setBorder(Rectangle.NO_BORDER);
                PdfPCell vCell = new PdfPCell(new Phrase(value, bodyFont));
                vCell.setBorder(Rectangle.NO_BORDER);
                table.addCell(lCell);
                table.addCell(vCell);
        }

        private void addResumenCell(PdfPTable table, String label, String value,
                        Font labelFont, Font bodyFont) {
                PdfPCell cell = new PdfPCell();
                cell.addElement(new Phrase(label, labelFont));
                cell.addElement(new Phrase(value, bodyFont));
                cell.setPadding(5f);
                cell.setBackgroundColor(new Color(245, 249, 255));
                table.addCell(cell);
        }

        private void addMovRow(PdfPTable table, Font font, Color bg,
                        String... values) {
                for (String val : values) {
                        PdfPCell cell = new PdfPCell(new Phrase(val, font));
                        cell.setBackgroundColor(bg);
                        cell.setPadding(3f);
                        table.addCell(cell);
                }
        }
}
