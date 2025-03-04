package com.company.TradingPlatform.wallet_service.Service;


import com.company.TradingPlatform.wallet_service.dtos.TransactionRequest;
import com.company.TradingPlatform.wallet_service.dtos.TransactionResponse;
import com.company.TradingPlatform.wallet_service.dtos.WalletResponse;
import com.company.TradingPlatform.wallet_service.entity.Transaction;
import com.company.TradingPlatform.wallet_service.entity.Wallet;
import com.company.TradingPlatform.wallet_service.enums.TransactionType;
import com.company.TradingPlatform.wallet_service.repository.TransactionRepository;
import com.company.TradingPlatform.wallet_service.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public WalletResponse createWallet(Long userId){
        log.info("Attempting to create wallet for userId: {}",userId);
        if(userId==null){
            log.error("Failed to create wallet for userId: {}",userId);
        }
        if(walletRepository.findByUserId(userId).isPresent()){
            throw new IllegalStateException("Wallet already exists");
        }
        Wallet wallet = Wallet.builder()
                .userId(userId)
                .balance(0.0)
                .build();

        walletRepository.save(wallet);
        log.info("Wallet created successfully for userId: {}", userId);
        return new WalletResponse(userId, 0.0, new ArrayList<>());
    }

    public WalletResponse getWallet(Long userId){
        log.info("Fetching wallet details for userId: {}",userId);
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(()->{
                    log.error("Wallet not found for userId: {}",userId);
                    return new IllegalArgumentException("Wallet not found");
                });
        List<TransactionResponse> transactions = wallet.getTransactions().stream()
                .map(tx -> new TransactionResponse(tx.getAmount(), tx.getType(), tx.getTimeStamp()))
                .collect(Collectors.toList());
        log.info("Successfully retrieved wallet for userId: {}",userId);
        return new WalletResponse(wallet.getUserId(),wallet.getBalance(),transactions);
    }

    @Transactional
    public TransactionResponse deposit(TransactionRequest request){
        log.error("Processing deposit: userId: {}, amount={}",request.getUserId(),request.getAmount());

        Wallet wallet = walletRepository.findByUserId(request.getUserId())
                .orElseThrow(()->{
                    log.error("Wallet not found for userId: {}",request.getUserId());
                    return new IllegalArgumentException("Wallet not found");
                });

        wallet.setBalance(wallet.getBalance()+request.getAmount());
        walletRepository.save(wallet);

        Transaction transaction  = Transaction.builder()
                .wallet(wallet)
                .amount(request.getAmount())
                .type(TransactionType.DEPOSIT)
                .timeStamp((LocalDateTime.now()))
                .build();
        transactionRepository.save(transaction);

        log.info("Deposit successful: userId={}, newBalance={}",request.getUserId(),wallet.getBalance());
        return new TransactionResponse(transaction.getAmount(), transaction.getType(),transaction.getTimeStamp());
    }

    @Transactional
    public TransactionResponse withdraw(TransactionRequest request){
        log.info("Processing withdrawal : userId={}, amount={}",request.getUserId(),request.getAmount());

        Wallet wallet = walletRepository.findByUserId(request.getUserId())
                .orElseThrow(()->{
                    log.error("Wallet not found for userId: {}",request.getUserId());
                    return new IllegalArgumentException("Wallet not found");
                });

        if(wallet.getBalance()< request.getAmount()){
            log.warn("Insufficient balance: userId={}, balance={}, withdrawal={}",
                    request.getUserId(),wallet.getBalance(),request.getAmount());
            throw new IllegalArgumentException("Insufficient balance");
        }
        wallet.setBalance(wallet.getBalance()- request.getAmount());
        walletRepository.save(wallet);

        Transaction transaction  = Transaction.builder()
                .wallet(wallet)
                .amount(request.getAmount())
                .type(TransactionType.WITHDRAWAL)
                .timeStamp((LocalDateTime.now()))
                .build();
        transactionRepository.save(transaction);
        log.info("Withdrawal successful: userId={}, newBalance={}", request.getUserId(),wallet.getBalance());
        return new TransactionResponse(transaction.getAmount(), transaction.getType(),transaction.getTimeStamp());
    }
}
