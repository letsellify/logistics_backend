package com.letsellify.logistics.components.logistics.core.kycManagement.data;

import com.letsellify.logistics.components.logistics.core.kycManagement.database.entity.KycEntity;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:2/3/25
 * Time:08:45
 */

@Data
public class LogisticKycDocument {
    private KycDocumentType kycDocument;

    private String kycIdentification;

    private String kycFilePath;

    public LogisticKycDocument(final KycEntity kycEntity) {
        this.kycDocument = kycEntity.getKycDocument();
        this.kycIdentification = kycEntity.getId();
        this.kycFilePath = kycEntity.getFilePath();
    }

//    public KycIdentifier getResource() {
//        return KycDataMapper.INSTANCE.toResource();
//    }
}
