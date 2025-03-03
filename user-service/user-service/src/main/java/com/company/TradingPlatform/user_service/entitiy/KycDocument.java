package com.company.TradingPlatform.user_service.entitiy;


import com.company.TradingPlatform.user_service.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "kyc_documents")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KycDocument {

    private Long id;
    private String userEmail;
    private String documentType;
    private String documentUrl;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status;

    @Lob
    private String verificationRemarks;

    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadedAt;
}
