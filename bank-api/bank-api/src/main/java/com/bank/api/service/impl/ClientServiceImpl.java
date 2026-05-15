package com.bank.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bank.api.dto.ClientDTO;
import com.bank.api.dto.ClientResponseDTO;
import com.bank.api.entity.Client;
import com.bank.api.repository.ClientRepository;
import com.bank.api.service.ClientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    @Override
    public ClientDTO create(ClientDTO dto) {

        Client client = new Client();

        client.setName(dto.getName());
        client.setGender(dto.getGender());
        client.setAge(dto.getAge());
        client.setIdentification(dto.getIdentification());
        client.setAddress(dto.getAddress());
        client.setPhone(dto.getPhone());
        client.setClientId(dto.getClientId());
        client.setPassword(dto.getPassword());
        client.setStatus(dto.getStatus());

        Client saved = repository.save(client);

        return mapToDTO(saved);
    }

    @Override
    public List<ClientResponseDTO> findAll() {

        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private ClientResponseDTO mapToDTO(Client client) {

        ClientResponseDTO dto = new ClientResponseDTO();

        dto.setName(client.getName());
        dto.setGender(client.getGender());
        dto.setAge(client.getAge());
        dto.setIdentification(client.getIdentification());
        dto.setAddress(client.getAddress());
        dto.setPhone(client.getPhone());
        dto.setClientId(client.getClientId());
        dto.setStatus(client.getStatus());
        dto.setId(client.getId());

        return dto;
    }
}