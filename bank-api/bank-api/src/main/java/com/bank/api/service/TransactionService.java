package com.bank.api.service;

import com.bank.api.dto.TransactionDTO;
import com.bank.api.entity.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction create(TransactionDTO dto);

    List<Transaction> findAll();
}