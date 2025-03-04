package com.company.TradingPlatform.wallet_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long userId;
    private Double balance;

    @OneToMany(mappedBy = "wallet",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Transaction> transactions;
}
