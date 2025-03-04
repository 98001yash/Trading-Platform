package com.company.TradingPlatform.wallet_service.controller;


import com.company.TradingPlatform.wallet_service.Service.WalletService;
import com.company.TradingPlatform.wallet_service.dtos.TransactionRequest;
import com.company.TradingPlatform.wallet_service.dtos.TransactionResponse;
import com.company.TradingPlatform.wallet_service.dtos.WalletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable Long userId){
        log.info("Fetching wallet details for userId: {}",userId);
        WalletResponse response = walletService.getWallet(userId);
        log.info("Wallet details retrieved successfully for userId: {}",userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody TransactionRequest request){
        log.info("Processing deposit for userId: {} with amount: {}",request.getUserId(),request.getAmount());
        TransactionResponse response = walletService.deposit(request);
        log.info("Deposit successful for userId: {},",request.getUserId());
        log.info("Deposit successful for userId: {}",request.getUserId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody TransactionRequest request){
        log.info("Processing withdrawal for userId: {} with amount: {}",request.getUserId(),request.getAmount());
        TransactionResponse response = walletService.withdraw(request);
        log.info("Withdrawal successful for userId: {},",request.getUserId());
        return ResponseEntity.ok(response);
    }
}
