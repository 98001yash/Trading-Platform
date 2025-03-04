package com.company.TradingPlatform.wallet_service.dtos;

import lombok.Data;

@Data
public class TransactionRequest {

    private Long userId;
    private Double amount;
}
