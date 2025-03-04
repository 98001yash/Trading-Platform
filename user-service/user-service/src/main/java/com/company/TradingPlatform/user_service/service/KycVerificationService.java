package com.company.TradingPlatform.user_service.service;


import com.company.TradingPlatform.user_service.entitiy.KycDocument;
import com.company.TradingPlatform.user_service.enums.VerificationStatus;
import com.company.TradingPlatform.user_service.repository.KycDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KycVerificationService {

    private final KycDocumentRepository kycDocumentRepository;

    public String verifyKyc(Long userId,boolean isApproved, String notes){
        Optional<KycDocument> documentOtp = kycDocumentRepository.findAllByUserId(userId);

        if(documentOtp.isEmpty()){
            return "No KYC document found for user";
        }

        KycDocument document = documentOtp.get();
        document.setStatus(isApproved ? VerificationStatus.VERIFIED : VerificationStatus.REJECTED);
        document.setVerificationRemarks(notes);
        kycDocumentRepository.save(document);
        return isApproved ? "KYC verified Successfully!": "KYC Rejected!";
    }

    public VerificationStatus getVerificationStatus(Long userId){
        return kycDocumentRepository.findAllByUserId(userId)
                .map(KycDocument::getStatus)
                .orElse(VerificationStatus.PENDING);
    }
}
