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
import com.bank.api.dto.ClientDTO;
import com.bank.api.dto.ClientResponseDTO;
import com.bank.api.service.ClientService;
import com.bank.api.util.LocaleUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;
    private final MessageSource messageSource;
    private final LocaleUtil localeUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<ClientDTO>> create(
            @Valid @RequestBody ClientDTO dto) {

        ClientDTO created = service.create(dto);
        String message = messageSource.getMessage("messages.client.created", null, localeUtil.getLocale());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.<ClientDTO>builder()
                        .data(created)
                        .code(201)
                        .message(message)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientResponseDTO>>> findAll() {

        List<ClientResponseDTO> clients = service.findAll();
        String message = messageSource.getMessage("messages.client.fetched", null, localeUtil.getLocale());
        return ResponseEntity.ok(ApiResponse.<List<ClientResponseDTO>>builder()
                .data(clients)
                .code(200)
                .message(message)
                .build());
    }
}