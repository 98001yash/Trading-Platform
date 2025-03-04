package com.company.TradingPlatform.wallet_service.controller;


import com.company.TradingPlatform.wallet_service.Service.WalletService;
import com.company.TradingPlatform.wallet_service.dtos.WalletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
@Slf4j
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<WalletResponse> createWallet(@PathVariable Long userId){
        log.info("Creating new wallet for userId: {}",userId);
        WalletResponse response = walletService.createWallet(userId);
        log.info("Wallet created successfully for userId: {}",userId);
        return ResponseEntity.ok(response);
    }
}
