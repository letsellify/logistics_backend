package com.letsellify.logistics.components.logistics.core.kyc.database.entity;

import java.util.UUID;

import com.letsellify.logistics.components.logistics.core.auditing.database.entity.Auditable;
import com.letsellify.logistics.components.logistics.core.kyc.data.KycDocument;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:12:46
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class KycEntity extends Auditable {
    @Id
    private long id;

    private KycDocument kycDocument;

    private String filePath;

    private UUID userId;

    private boolean approved;

    protected KycEntity() {
        super();
    }

    public static KycEntity getInstance(final UUID userId, final KycDocument kycDocument) {
        final KycEntity kycEntity = new KycEntity();
        kycEntity.setUserId(userId);
        kycEntity.setKycDocument(kycDocument);
        kycEntity.setFilePath(null);
        kycEntity.setApproved(false);
        return kycEntity;
    }

}
