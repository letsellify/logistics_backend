package com.letsellify.logistics.components.logistic.core.kyc.database.entity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import com.letsellify.logistics.components.logistic.core.kyc.data.KycDocumentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AHMAD BUBA
 * Date:2/1/25
 * Time:11:02
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class KycEntity extends Auditable {
    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private KycDocumentType kycDocument;

    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_kyc_collection_id", nullable = false)
    private UserKycCollectionEntity userKycCollection;

    protected KycEntity() {
        super();
    }

    public KycEntity(final KycDocumentType kycDocument, final String filePath) {
        super();
        this.id = this.generateKycId(filePath);
        this.kycDocument = kycDocument;
        this.filePath = filePath;
    }

    private String generateKycId(final String filePath) {
        try {
            // Generate random UUID
            final String randomUUID = UUID.randomUUID().toString();

            // Hash the file path
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hashBytes = digest.digest(filePath.getBytes(StandardCharsets.UTF_8));
            final String hashedPath = Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes).substring(0, 8);

            // Timestamp
            final String timestamp = String.valueOf(Instant.now().toEpochMilli());

            // Combine for final ID
            return randomUUID + "-" + hashedPath + "-" + timestamp;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate KYC ID", e);
        }

    }

}
