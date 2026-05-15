package com.bank.api.controller;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.api.dto.ApiResponse;
import com.bank.api.dto.TransactionDTO;
import com.bank.api.entity.Transaction;
import com.bank.api.service.TransactionService;
import com.bank.api.util.LocaleUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;
    private final MessageSource messageSource;
    private final LocaleUtil localeUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<Transaction>> create(
            @Valid @RequestBody TransactionDTO dto) {

        Transaction created = service.create(dto);
        String message = messageSource.getMessage("messages.transaction.created", null, localeUtil.getLocale());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.<Transaction>builder()
                        .data(created)
                        .code(201)
                        .message(message)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Transaction>>> findAll() {

        List<Transaction> transactions = service.findAll();
        String message = messageSource.getMessage("messages.transaction.fetched", null, localeUtil.getLocale());
        return ResponseEntity.ok(ApiResponse.<List<Transaction>>builder()
                .data(transactions)
                .code(200)
                .message(message)
                .build());
    }
}