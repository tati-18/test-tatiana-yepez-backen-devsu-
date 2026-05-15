package com.bank.api.service;

import java.util.List;

import com.bank.api.dto.AccountDTO;
import com.bank.api.dto.AccountResponseDTO;

public interface AccountService {

    AccountDTO create(AccountDTO dto);

    List<AccountResponseDTO> findAll();
}