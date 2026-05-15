package com.bank.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bank.api.dto.AccountDTO;
import com.bank.api.dto.AccountResponseDTO;
import com.bank.api.entity.Account;
import com.bank.api.entity.Client;
import com.bank.api.repository.AccountRepository;
import com.bank.api.repository.ClientRepository;
import com.bank.api.service.AccountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    @Override
    public AccountDTO create(AccountDTO dto) {

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(()
                        -> new RuntimeException("Client not found"));

        Account account = new Account();

        account.setAccountNumber(dto.getAccountNumber());
        account.setAccountType(dto.getAccountType());
        account.setInitialBalance(dto.getInitialBalance());
        account.setCurrentBalance(dto.getInitialBalance());
        account.setStatus(dto.getStatus());
        account.setClient(client);

        Account saved = accountRepository.save(account);

        return mapToDTO(saved);
    }

    @Override
    public List<AccountResponseDTO> findAll() {

        return accountRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private AccountResponseDTO mapToDTO(Account account) {

        AccountResponseDTO dto = new AccountResponseDTO();

        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getAccountType());
        dto.setInitialBalance(account.getInitialBalance());
        dto.setStatus(account.getStatus());
        dto.setClientId(account.getClient().getId());
        dto.setId(account.getId());
        return dto;
    }
}
