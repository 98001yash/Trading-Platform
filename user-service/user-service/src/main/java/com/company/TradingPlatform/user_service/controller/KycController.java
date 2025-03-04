package com.company.TradingPlatform.user_service.controller;


import com.company.TradingPlatform.user_service.entitiy.KycDocument;
import com.company.TradingPlatform.user_service.enums.VerificationStatus;
import com.company.TradingPlatform.user_service.repository.KycDocumentRepository;
import com.company.TradingPlatform.user_service.service.FileStorageService;
import com.company.TradingPlatform.user_service.service.KycVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@RequestMapping("/kyc")
@RequiredArgsConstructor
public class KycController {

    private final FileStorageService fileStorageService;
    private final KycDocumentRepository kycDocumentRepository;
    private final KycVerificationService kycVerificationService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadKycDocument(@RequestParam("userId") Long userId,
                                                    @RequestParam("email") String email,
                                                    @RequestParam("documentType") String documentType,
                                                    @RequestParam("file") MultipartFile file) {

        System.out.println("Received User ID: " + userId); // Debugging Log

        String filePath = fileStorageService.saveFileLocally(file, email); // Save to local storage

        KycDocument document = KycDocument.builder()
                .userId(userId)  // ✅ Ensure userId is stored
                .userEmail(email)
                .documentType(documentType)
                .documentUrl(filePath)
                .status(VerificationStatus.PENDING)
                .uploadedAt(new Date())
                .build();

        kycDocumentRepository.save(document);

        return ResponseEntity.ok("Document saved successfully for user: " + userId);
    }



    // verify or Reject KYC (Admin or Automated System)
    @PostMapping("/verify/{userId}")
    public ResponseEntity<String> verifyKyc(@PathVariable Long userId,
                                            @RequestParam("approved") boolean approved,
                                            @RequestParam(value = "notes", required = false) String notes){
        return ResponseEntity.ok(kycVerificationService.verifyKyc(userId,approved,notes));
    }

    // get User's KYC status
    @GetMapping("/status/{userId}")
    public ResponseEntity<VerificationStatus> getKyxStatus(@PathVariable Long userId){
        return ResponseEntity.ok(kycVerificationService.getVerificationStatus(userId));
    }
}