package com.bank.api.service;

import java.util.List;

import com.bank.api.dto.ClientDTO;
import com.bank.api.dto.ClientResponseDTO;

public interface ClientService {

    ClientDTO create(ClientDTO dto);

    List<ClientResponseDTO> findAll();
}