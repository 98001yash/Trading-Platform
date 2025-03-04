package com.company.TradingPlatform.wallet_service.dtos;

import com.company.TradingPlatform.wallet_service.enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private Double amount;
    private TransactionType type;
    private LocalDateTime timeStamp;
}
