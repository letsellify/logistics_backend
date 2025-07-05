package com.letsellify.logistics.components.logistic.core.request.database.entity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AHMAD BUBA
 * Date:2/8/25
 * Time:03:13
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class LogisticItemImageEntity extends Auditable {
    @Id
    private String id;

    @Column(nullable = false)
    private String imageFilePath;

    @Column(nullable = false)
    private String senderUsername;

    @ManyToOne
    @JoinColumn(name = "logistics_request")
    private LogisticRequestEntity logisticsRequest;

    protected LogisticItemImageEntity() {
        super();
    }

    public LogisticItemImageEntity(final String userEmail, final String imageFilePath) {
        super();
        this.id = this.generateImageId(imageFilePath);
        this.imageFilePath = imageFilePath;
        this.senderUsername = userEmail;
    }

    private String generateImageId(final String filePath) {
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
