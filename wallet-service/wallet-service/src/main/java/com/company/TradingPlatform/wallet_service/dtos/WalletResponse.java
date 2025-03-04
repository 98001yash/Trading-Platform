package com.company.TradingPlatform.wallet_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletResponse {

   private Long userId;
   private Double balance;
   private List<TransactionResponse> transactions;

}
