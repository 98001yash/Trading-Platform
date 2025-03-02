package com.company.TradingPlatform.user_service.service;

import com.company.TradingPlatform.user_service.entitiy.OtpEntity;
import com.company.TradingPlatform.user_service.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;
    private final EmailService emailService;

    private static final int OTP_EXPIRY_MINUTES = 10;

    public String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // Generate 6-digit OTP

        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));

        otpRepository.save(otpEntity);
        emailService.sendOtp(email, otp);

        return "OTP sent to " + email;
    }

    public boolean validateOtp(String email, String otp) {
        Optional<OtpEntity> otpEntity = otpRepository.findByEmail(email);

        return otpEntity.isPresent() &&
                otpEntity.get().getOtp().equals(otp) &&
                otpEntity.get().getExpiryTime().isAfter(LocalDateTime.now());
    }
}
