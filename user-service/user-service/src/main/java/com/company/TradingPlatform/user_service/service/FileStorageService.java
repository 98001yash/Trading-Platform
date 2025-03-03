package com.company.TradingPlatform.user_service.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    @Value("${local.storage.path}")
    private String storagePath;

    public String saveFileLocally(MultipartFile file, String email) {
        try {
            String useDir = storagePath + "/" + email;
            File directory = new File(useDir);
            if (!directory.exists()) {
                directory.mkdirs(); // create user-specific directory
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Path.of(useDir, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filePath.toString(); // Return the local file path
        } catch (IOException e) {
            log.error("Error saving file locally", e);
            throw new RuntimeException("File storage failed");
        }
    }
}
