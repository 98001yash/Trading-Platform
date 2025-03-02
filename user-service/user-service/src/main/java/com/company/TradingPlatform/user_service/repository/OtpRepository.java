package com.company.TradingPlatform.user_service.repository;

import com.company.TradingPlatform.user_service.entitiy.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpEntity ,Long> {

    Optional<OtpEntity> findByEmail(String email);
}
