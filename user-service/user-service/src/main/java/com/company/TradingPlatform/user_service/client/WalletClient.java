package com.company.TradingPlatform.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "wallet-service", url = "http://localhost:9030")  // Correct port
public interface WalletClient {

    @PostMapping("/api/wallets/create/{userId}")  // Fixed Path Variable syntax
    void createWallet(@PathVariable Long userId);
}
