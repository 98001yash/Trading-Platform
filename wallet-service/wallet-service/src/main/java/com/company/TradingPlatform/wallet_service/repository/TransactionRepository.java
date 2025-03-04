package com.company.TradingPlatform.wallet_service.repository;

import com.company.TradingPlatform.wallet_service.entity.Transaction;
import com.company.TradingPlatform.wallet_service.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long {
    List<Transaction> findByWallet(Wallet wallet);
}
