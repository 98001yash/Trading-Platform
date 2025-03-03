package com.company.TradingPlatform.user_service.controller;


import com.company.TradingPlatform.user_service.entitiy.KycDocument;
import com.company.TradingPlatform.user_service.enums.VerificationStatus;
import com.company.TradingPlatform.user_service.repository.KycDocumentRepository;
import com.company.TradingPlatform.user_service.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@RequestMapping("/kyc")
@RequiredArgsConstructor
public class KycController {

    private final FileStorageService fileStorageService;
    private final KycDocumentRepository kycDocumentRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadKycDocument(@RequestParam("email") String email,
                                                    @RequestParam("documentType") String documentType,
                                                    @RequestParam("file") MultipartFile file) {

        String filePath = fileStorageService.saveFileLocally(file, email); // Save to local storage

        KycDocument document = KycDocument.builder()
                .userEmail(email)
                .documentType(documentType)
                .documentUrl(filePath) // Store local file path
                .status(VerificationStatus.PENDING)
                .uploadedAt(new Date())
                .build();

        kycDocumentRepository.save(document);

        return ResponseEntity.ok("Document saved successfully: " + filePath);
    }
}