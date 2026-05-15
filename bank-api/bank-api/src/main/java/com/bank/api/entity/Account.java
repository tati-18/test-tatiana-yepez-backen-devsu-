package com.bank.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String accountNumber;

    private String accountType;

    private BigDecimal initialBalance;

    private BigDecimal currentBalance;

    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}