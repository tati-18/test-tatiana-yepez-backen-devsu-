package com.bank.api.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bank.api.dto.ApiResponse;
import com.bank.api.util.LocaleUtil;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;
    private final LocaleUtil localeUtil;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {

        String message = messageSource.getMessage("messages.error.invalid", null, localeUtil.getLocale());
        
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.builder()
                        .data(null)
                        .code(400)
                        .message(message)
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(
            MethodArgumentNotValidException ex) {

        String message = messageSource.getMessage("messages.error.invalid", null, localeUtil.getLocale());
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()));

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.builder()
                        .data(errors)
                        .code(400)
                        .message(message)
                        .build());
    }
}