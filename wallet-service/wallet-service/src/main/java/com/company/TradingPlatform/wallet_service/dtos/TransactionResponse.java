package com.company.TradingPlatform.wallet_service.dtos;

import com.company.TradingPlatform.wallet_service.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {

    private Double amount;
    private TransactionType type;
    private LocalDateTime timeStamp;
}
