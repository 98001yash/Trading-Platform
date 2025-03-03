package com.company.TradingPlatform.user_service.repository;

import com.company.TradingPlatform.user_service.entitiy.KycDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  KycDocumentRepository extends JpaRepository<KycDocument,Long> {
    List<KycDocument> findByUserEmail(String email);
}
