package com.company.TradingPlatform.wallet_service.dtos;


import com.company.TradingPlatform.wallet_service.enums.TransactionType;
import lombok.Data;

import java.util.List;


@Data
public class WalletResponse {

    private Double amount;
    private TransactionType type;
   private List<TransactionResponse> transactions;
}
