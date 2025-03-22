package com.letsellify.logistics.components.user.core.verificationCode.database.entity;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import com.letsellify.logistics.common.data.LogisticAppRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AHMAD BUBA
 * Date:2/10/25
 * Time:10:10
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class VerificationCodeEntity extends Auditable {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static final int CODE_EXPIRY_MINUTES = 15;

    @Transient
    private static final int CODE_LENGTH = 6;

    @Id
    private UUID id;

    private String code;

    private String userEmail;

    private LogisticAppRole role;

    private boolean verified = false; // Indicates if the code was successfully used

    private boolean expired = false; // Indicates if the code is expired

    @Column(nullable = false)
    private LocalDateTime createdAt; // When the code was generated

    @Column(nullable = false)
    private LocalDateTime expiresAt; // Expiry time for the code


    protected VerificationCodeEntity() {
        super();
    }

    public VerificationCodeEntity(final String userEmail, final LogisticAppRole role) {
        super();
        this.id = UUID.randomUUID();
        this.code = this.generateVerificationCode();
        this.userEmail = userEmail;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusMinutes(CODE_EXPIRY_MINUTES);
    }


    private String generateVerificationCode() {
        final int min = (int) Math.pow(10, CODE_LENGTH - 1); // 100000
        final int max = (int) Math.pow(10, CODE_LENGTH) - 1; // 999999

        final int code = secureRandom.nextInt((max - min) + 1) + min;
        return String.valueOf(code);
    }

    public void setToExpired() {
        this.expired = true;
    }

    public void setToVerified() {
        this.verified = true;
    }

}

