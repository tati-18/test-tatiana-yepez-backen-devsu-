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

import com.bank.api.dto.AccountDTO;
import com.bank.api.dto.AccountResponseDTO;
import com.bank.api.dto.ApiResponse;
import com.bank.api.service.AccountService;
import com.bank.api.util.LocaleUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;
    private final MessageSource messageSource;
    private final LocaleUtil localeUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountDTO>> create(
            @Valid @RequestBody AccountDTO dto) {

        AccountDTO created = service.create(dto);
        String message = messageSource.getMessage("messages.account.created", null, localeUtil.getLocale());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.<AccountDTO>builder()
                        .data(created)
                        .code(201)
                        .message(message)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponseDTO>>> findAll() {

        List<AccountResponseDTO> accounts = service.findAll();
        String message = messageSource.getMessage("messages.account.fetched", null, localeUtil.getLocale());
        return ResponseEntity.ok(ApiResponse.<List<AccountResponseDTO>>builder()
                .data(accounts)
                .code(200)
                .message(message)
                .build());
    }
}